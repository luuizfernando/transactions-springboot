package com.luiz.transactions.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(

    @Schema(description = "Nome de usuário", example = "luiz")
    @NotBlank(message = "O nome de usuário é obrigatório")
    String name,

    @Schema(description = "Senha do usuário", example = "123456")
    @NotBlank(message = "A senha é obrigatória")
    String password
    
) {}