package com.luiz.transactions.dto;

import java.util.UUID;

public record AccountResponseDTO(
    UUID id,
    UUID userId
) {}