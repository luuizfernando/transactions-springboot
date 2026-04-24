package com.luiz.transactions.domain.transaction.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

public record TransactionAnalyzeRequestDTO(

    @Schema(description = "ID da conta que realizará a transação", example = "550e8400-e29b-41d4-a716-446655440000")
    @NotNull(message = "O ID da conta é obrigatório")
    UUID accountId,

    @Schema(description = "Valor da transação a ser analisada", example = "1500.00")
    @NotNull(message = "O valor da transação é obrigatório")
    @Positive(message = "O valor deve ser maior que zero")
    BigDecimal amount,

    @Schema(description = "Descrição textual da transação", example = "Compra de notebook gamer")
    String description
    
) {}