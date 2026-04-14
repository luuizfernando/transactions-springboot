package com.luiz.transactions.service;

import java.util.List;
import java.util.UUID;

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
    public TransactionResponseDTO deposit(DepositRequestDTO data) {
        Account account = getAccountOrThrow(data.accountId());
        account.setBalance(account.getBalance().add(data.amount()));

        Transaction transaction = Transaction.deposit(account, data.amount());
        transactionRepository.save(transaction);
        return toResponse(transaction);
    }

    @Transactional
    public TransactionResponseDTO transfer(TransferRequestDTO data) {
        Account fromAccount = getAccountOrThrow(data.fromAccountId());
        Account toAccount = getAccountOrThrow(data.toAccountId());

        if (fromAccount.getBalance().compareTo(data.amount()) < 0) {
            throw new InsufficientFundsException("Saldo insuficiente para realizar a transferência.");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(data.amount()));
        toAccount.setBalance(toAccount.getBalance().add(data.amount()));

        Transaction transaction = Transaction.transfer(fromAccount, toAccount, data.amount());
        transactionRepository.save(transaction);
        return toResponse(transaction);
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
                transaction.getCreatedAt());
    }

}