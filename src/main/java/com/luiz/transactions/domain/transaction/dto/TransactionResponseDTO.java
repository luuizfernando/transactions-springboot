package com.luiz.transactions.domain.transaction.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.luiz.transactions.domain.transaction.enums.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;

public record TransactionResponseDTO(
        
        @Schema(description = "Identificador único da transação", example = "550e8400-e29b-41d4-a716-446655440002")
        UUID id,
        
        @Schema(description = "Tipo de transação", example = "TRANSFER")
        TransactionType type,
        
        @Schema(description = "ID da conta de origem", example = "550e8400-e29b-41d4-a716-446655440001")
        UUID fromAccountId,
        
        @Schema(description = "ID da conta de destino", example = "550e8400-e29b-41d4-a716-446655440003")
        UUID toAccountId,
        
        @Schema(description = "Valor da transação", example = "25.00")
        BigDecimal amount,
        
        @Schema(description = "Descrição da transação", example = "Pagamento almoço")
        String description,
        
        @Schema(description = "Data e hora de criação da transação")
        Instant createdAt

) {}