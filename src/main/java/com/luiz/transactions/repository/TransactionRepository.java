package com.luiz.transactions.repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.luiz.transactions.domain.transaction.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    Optional<Transaction> findByIdempotencyKey(String idempotencyKey);

    @Query("SELECT t FROM Transaction t WHERE t.fromAccount.id = :accountId OR t.toAccount.id = :accountId")
    List<Transaction> findByAccountId(@Param("accountId") UUID accountId);

    @Query("SELECT AVG(t.amount) FROM Transaction t WHERE t.toAccount.id = :accountId OR t.fromAccount.id = :accountId")
    Optional<BigDecimal> findAverageAmountByAccountId(@Param("accountId") UUID accountId);
    
}