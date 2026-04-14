package com.luiz.transactions.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record BalanceResponseDTO(
        UUID accountId,
        BigDecimal balance
) {}