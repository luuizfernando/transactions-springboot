package com.luiz.transactions.domain.transaction.dto;

import java.math.BigDecimal;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record TransferRequestDTO(
        
        @Schema(description = "ID da conta de origem", example = "550e8400-e29b-41d4-a716-446655440001")
        @NotNull UUID fromAccountId,
        
        @Schema(description = "ID da conta de destino", example = "550e8400-e29b-41d4-a716-446655440003")
        @NotNull UUID toAccountId,
        
        @Schema(description = "Valor a ser transferido", example = "30.00")
        @NotNull @DecimalMin(value = "0.01", inclusive = true) BigDecimal amount,
        
        @Schema(description = "Descrição da transferência", example = "Transferência para João")
        @NotNull String description

) {}