package com.luiz.transactions.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record CreateAccountRequestDTO(
        @NotNull UUID userId
) {}