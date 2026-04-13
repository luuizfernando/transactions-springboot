package com.luiz.transactions.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luiz.transactions.domain.account.Account;
import com.luiz.transactions.domain.user.User;
import com.luiz.transactions.dto.AccountResponse;
import com.luiz.transactions.dto.CreateAccountRequest;
import com.luiz.transactions.exception.ResourceNotFoundException;
import com.luiz.transactions.repository.AccountRepository;
import com.luiz.transactions.repository.UserRepository;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public AccountResponse create(CreateAccountRequest data) {        
        User user = userRepository.findById(data.userId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));
        Account account = new Account();
        account.setUser(user);
        accountRepository.save(account);
        return new AccountResponse(account.getId(), user.getId());
    }

}