package com.luiz.transactions.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponseDTO(

    @Schema(description = "Token JWT para autenticação", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    String token
    
) {}