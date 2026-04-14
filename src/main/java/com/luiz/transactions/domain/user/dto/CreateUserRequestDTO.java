package com.luiz.transactions.domain.user.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateUserRequestDTO(
        @NotBlank String name
) {}