package com.luiz.transactions.domain.account.dto;

import java.util.UUID;

public record AccountResponseDTO(
    UUID id,
    UUID userId
) {}