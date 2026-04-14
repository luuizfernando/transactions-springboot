package com.luiz.transactions.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateUserRequestDTO(
        @NotBlank String name
) {}