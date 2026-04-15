package com.luiz.transactions.domain.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.UUID;

public record BalanceResponseDTO(

        @Schema(description = "Identificador da conta", example = "550e8400-e29b-41d4-a716-446655440001")
        UUID accountId,
        
        @Schema(description = "Saldo atual da conta", example = "100.50")
        BigDecimal balance

) {}