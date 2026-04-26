package com.luiz.transactions.domain.transaction.dto;

import java.math.BigDecimal;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TransferRequestDTO(
        
        @Schema(description = "ID da conta de origem", example = "550e8400-e29b-41d4-a716-446655440001")
        @NotNull UUID fromAccountId,
        
        @Schema(description = "ID da conta de destino", example = "550e8400-e29b-41d4-a716-446655440003")
        @NotNull UUID toAccountId,
        
        @Schema(description = "Valor a ser transferido", example = "30.00")
        @NotNull @DecimalMin(value = "0.01", inclusive = true) @DecimalMax(value = "1000000000", message = "O valor máximo de transferência é de 1 bilhão") BigDecimal amount,
        
        @Schema(description = "Descrição da transferência", example = "Transferência para João")
        @NotNull @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres") String description

) {
    @AssertTrue(message = "A conta de origem deve ser diferente da conta de destino.")
    public boolean isDifferentAccounts() {
        return fromAccountId == null || toAccountId == null || !fromAccountId.equals(toAccountId);
    }
}