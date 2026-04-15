package com.luiz.transactions.domain.user.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(

    @NotBlank(message = "O nome de usuário é obrigatório")
    String name,

    @NotBlank(message = "A senha é obrigatória")
    String password
    
) {}