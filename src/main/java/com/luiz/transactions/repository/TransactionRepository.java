package com.luiz.transactions.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luiz.transactions.domain.transaction.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    
}