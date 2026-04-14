package com.luiz.transactions.domain.account.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record BalanceResponseDTO(
        UUID accountId,
        BigDecimal balance
) {}