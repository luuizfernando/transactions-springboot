package com.luiz.transactions.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

import org.jspecify.annotations.Nullable;

public record UserResponseDTO(
    
    @Schema(description = "Identificador único do usuário", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID id,
    
    @Schema(description = "Nome do usuário", example = "luiz")
    String name,
    
    @Schema(
        description = "Identificador da conta vinculada ao usuário, se existir",
        example = "550e8400-e29b-41d4-a716-446655440001",
        nullable = true
    )
    @Nullable UUID accountId

) {}