package com.luiz.transactions.domain.transaction.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

public record TransactionAnalyzeRequestDTO(

    @NotNull(message = "O ID da conta é obrigatório")
    UUID accountId,

    @NotNull(message = "O valor da transação é obrigatório")
    @Positive(message = "O valor deve ser maior que zero")
    BigDecimal amount,

    String description
    
) {}