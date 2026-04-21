package com.luiz.transactions.repository;

import java.util.Optional;
import java.util.UUID;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.luiz.transactions.domain.account.Account;
import com.luiz.transactions.domain.user.User;

public interface AccountRepository extends JpaRepository<Account, UUID> {

    boolean existsByUserId(UUID userId);

    boolean existsByIdAndUser(UUID id, User user);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Account a WHERE a.id = :id")
    Optional<Account> findByIdWithLock(@Param("id") UUID id);

    Optional<Account> findByUserId(UUID userId);
    
}