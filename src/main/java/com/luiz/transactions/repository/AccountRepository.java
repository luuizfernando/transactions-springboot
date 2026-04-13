package com.luiz.transactions.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luiz.transactions.domain.account.Account;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    
}