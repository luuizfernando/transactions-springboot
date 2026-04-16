package com.luiz.transactions.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luiz.transactions.domain.account.Account;
import com.luiz.transactions.domain.account.dto.AccountResponseDTO;
import com.luiz.transactions.domain.user.User;
import com.luiz.transactions.exception.ConflictException;
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

    /**
     * Cria uma conta vinculada ao usuário autenticado. Cada usuário pode ter no máximo uma conta.
     */
    @Transactional
    public AccountResponseDTO createAccountForUser(User principal) {
        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        if (accountRepository.existsByUserId(user.getId())) {
            throw new ConflictException("Este usuário já possui uma conta.");
        }

        Account account = new Account(user);
        account = accountRepository.save(account);
        user.setAccount(account);
        userRepository.save(user);

        return new AccountResponseDTO(account.getId(), user.getId());
    }
    
}