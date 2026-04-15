package com.luiz.transactions.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record RegisterDTO(

        @Schema(description = "Nome de usuário para registro", example = "luiz")
        @NotBlank(message = "O nome de usuário é obrigatório")
        String name,
        
        @Schema(description = "Senha do usuário para registro", example = "123456")
        @NotBlank(message = "A senha é obrigatória")
        String password
        
) {}