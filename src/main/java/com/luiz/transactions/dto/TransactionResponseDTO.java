package com.luiz.transactions.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.luiz.transactions.domain.transaction.TransactionType;

public record TransactionResponseDTO(
        UUID id,
        TransactionType type,
        UUID fromAccountId,
        UUID toAccountId,
        BigDecimal amount,
        Instant createdAt
) {}