package com.luiz.transactions.domain.user.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterDTO(

        @NotBlank String name,
        @NotBlank String password
        
) {}