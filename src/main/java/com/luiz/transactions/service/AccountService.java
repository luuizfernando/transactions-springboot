package com.luiz.transactions.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luiz.transactions.domain.account.Account;
import com.luiz.transactions.domain.account.dto.AccountResponseDTO;
import com.luiz.transactions.domain.user.User;
import com.luiz.transactions.exception.ConflictException;
import com.luiz.transactions.repository.AccountRepository;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Cria uma conta vinculada ao usuário autenticado. Cada usuário pode ter no máximo uma conta.
     */
    @Transactional
    public AccountResponseDTO createAccountForUser(User user) {
        if (accountRepository.existsByUserId(user.getId())) {
            throw new ConflictException("Este usuário já possui uma conta.");
        }

        Account account = new Account(user);
        account = accountRepository.save(account);
        user.setAccount(account);

        return new AccountResponseDTO(account.getId(), user.getId());
    }
    
}