package com.luiz.transactions.domain.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import jakarta.validation.constraints.NotNull;

public record CreateAccountRequestDTO(

        @Schema(description = "ID do usuário para quem a conta será criada", example = "550e8400-e29b-41d4-a716-446655440000")
        @NotNull UUID userId
        
) {}