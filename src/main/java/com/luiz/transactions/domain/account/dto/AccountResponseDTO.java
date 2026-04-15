package com.luiz.transactions.domain.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

public record AccountResponseDTO(
    
    @Schema(description = "Identificador único da conta", example = "550e8400-e29b-41d4-a716-446655440001")
    UUID id,
    
    @Schema(description = "Identificador do usuário dono da conta", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID userId

) {}