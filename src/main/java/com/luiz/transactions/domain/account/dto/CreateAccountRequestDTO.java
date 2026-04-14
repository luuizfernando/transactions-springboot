package com.luiz.transactions.domain.account.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record CreateAccountRequestDTO(
        @NotNull UUID userId
) {}