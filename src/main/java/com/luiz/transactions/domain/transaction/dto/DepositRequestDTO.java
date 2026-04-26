package com.luiz.transactions.domain.transaction.dto;

import java.math.BigDecimal;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DepositRequestDTO(
        
        @Schema(description = "ID da conta que receberá o depósito", example = "550e8400-e29b-41d4-a716-446655440001")
        @NotNull UUID accountId,
        
        @Schema(description = "Valor a ser depositado", example = "50.00")
        @NotNull @DecimalMin(value = "0.01", inclusive = true) @DecimalMax(value = "1000000000", message = "O valor máximo de depósito é de 1 bilhão") BigDecimal amount,
        
        @Schema(description = "Descrição do depósito", example = "Depósito inicial")
        @NotNull @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres") String description

) {}