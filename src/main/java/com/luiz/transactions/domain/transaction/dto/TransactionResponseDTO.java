package com.luiz.transactions.domain.transaction.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.luiz.transactions.domain.transaction.enums.TransactionType;


public record TransactionResponseDTO(
        UUID id,
        TransactionType type,
        UUID fromAccountId,
        UUID toAccountId,
        BigDecimal amount,
        String description,
        Instant createdAt
) {}