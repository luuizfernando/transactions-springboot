package com.luiz.transactions.domain.transaction.dto;

import java.math.BigDecimal;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record DepositRequestDTO(
        
        @Schema(description = "ID da conta que receberá o depósito", example = "550e8400-e29b-41d4-a716-446655440001")
        @NotNull UUID accountId,
        
        @Schema(description = "Valor a ser depositado", example = "50.00")
        @NotNull @DecimalMin(value = "0.01", inclusive = true) BigDecimal amount,
        
        @Schema(description = "Descrição do depósito", example = "Depósito inicial")
        @NotNull String description

) {}