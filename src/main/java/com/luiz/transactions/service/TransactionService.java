package com.luiz.transactions.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luiz.transactions.domain.account.Account;
import com.luiz.transactions.domain.account.dto.BalanceResponseDTO;
import com.luiz.transactions.domain.transaction.Transaction;
import com.luiz.transactions.domain.transaction.dto.DepositRequestDTO;
import com.luiz.transactions.domain.transaction.dto.TransactionResponseDTO;
import com.luiz.transactions.domain.transaction.dto.TransferRequestDTO;
import com.luiz.transactions.exception.InsufficientFundsException;
import com.luiz.transactions.exception.ResourceNotFoundException;
import com.luiz.transactions.repository.AccountRepository;
import com.luiz.transactions.repository.TransactionRepository;

@Service
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public TransactionResponseDTO deposit(DepositRequestDTO data, String idempotencyKey) {
        Account account = getAccountOrThrowWithLock(data.accountId());

        Optional<Transaction> existingTransaction = findByIdempotencyKey(idempotencyKey);
        if (existingTransaction.isPresent()) {
            return toResponse(existingTransaction.get());
        }

        account.setBalance(account.getBalance().add(data.amount()));

        Transaction transaction = Transaction.deposit(account, data.amount(), data.description(), normalizeIdempotencyKey(idempotencyKey));
        return saveTransactionWithIdempotencyFallback(transaction, idempotencyKey);
    }

    @Transactional
    public TransactionResponseDTO transfer(TransferRequestDTO data, String idempotencyKey) {
        UUID fromAccountId = data.fromAccountId();
        UUID toAccountId = data.toAccountId();

        boolean lockFromFirst = fromAccountId.compareTo(toAccountId) <= 0;
        UUID firstLockId = lockFromFirst ? fromAccountId : toAccountId;
        UUID secondLockId = lockFromFirst ? toAccountId : fromAccountId;

        Account firstLockedAccount = getAccountOrThrowWithLock(firstLockId);
        Account secondLockedAccount = getAccountOrThrowWithLock(secondLockId);

        Optional<Transaction> existingTransaction = findByIdempotencyKey(idempotencyKey);
        
        if (existingTransaction.isPresent()) {
            return toResponse(existingTransaction.get());
        }

        Account fromAccount = lockFromFirst ? firstLockedAccount : secondLockedAccount;
        Account toAccount = lockFromFirst ? secondLockedAccount : firstLockedAccount;

        if (fromAccount.getBalance().compareTo(data.amount()) < 0) {
            throw new InsufficientFundsException("Saldo insuficiente para realizar a transferência.");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(data.amount()));
        toAccount.setBalance(toAccount.getBalance().add(data.amount()));

        Transaction transaction = Transaction.transfer(
            fromAccount, toAccount, data.amount(), data.description(), normalizeIdempotencyKey(idempotencyKey)
        );

        return saveTransactionWithIdempotencyFallback(transaction, idempotencyKey);
    }

    @Transactional(readOnly = true)
    public BalanceResponseDTO getBalance(UUID accountId) {
        Account account = getAccountOrThrow(accountId);
        return new BalanceResponseDTO(account.getId(), account.getBalance());
    }

    @Transactional(readOnly = true)
    public List<TransactionResponseDTO> listTransactions(UUID accountId) {
        getAccountOrThrow(accountId);
        return transactionRepository.findByAccountId(accountId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private Account getAccountOrThrow(UUID accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Conta não encontrada."));
    }

    private Account getAccountOrThrowWithLock(UUID accountId) {
        return accountRepository.findByIdWithLock(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Conta não encontrada."));
    }

    private Optional<Transaction> findByIdempotencyKey(String idempotencyKey) {
        String normalizedIdempotencyKey = normalizeIdempotencyKey(idempotencyKey);
        if (normalizedIdempotencyKey == null) {
            return Optional.empty();
        }
        return transactionRepository.findByIdempotencyKey(normalizedIdempotencyKey);
    }

    private String normalizeIdempotencyKey(String idempotencyKey) {
        if (idempotencyKey == null) {
            return null;
        }
        String normalizedIdempotencyKey = idempotencyKey.trim();
        return normalizedIdempotencyKey.isEmpty() ? null : normalizedIdempotencyKey;
    }

    private TransactionResponseDTO saveTransactionWithIdempotencyFallback(Transaction transaction, String idempotencyKey) {
        String normalizedIdempotencyKey = normalizeIdempotencyKey(idempotencyKey);
        try {
            transactionRepository.save(transaction);
            return toResponse(transaction);
        } catch (DataIntegrityViolationException ex) {
            if (normalizedIdempotencyKey == null) {
                throw ex;
            }

            return transactionRepository.findByIdempotencyKey(normalizedIdempotencyKey)
                    .map(this::toResponse)
                    .orElseThrow(() -> ex);
        }
    }

    private TransactionResponseDTO toResponse(Transaction transaction) {
        UUID fromAccountId = transaction.getFromAccount() != null
                ? transaction.getFromAccount().getId()
                : null;
        UUID toAccountId = transaction.getToAccount() != null
                ? transaction.getToAccount().getId()
                : null;
        return new TransactionResponseDTO(
                transaction.getId(),
                transaction.getType(),
                fromAccountId,
                toAccountId,
                transaction.getAmount(),
                transaction.getDescription(),
                transaction.getCreatedAt());
    }

}