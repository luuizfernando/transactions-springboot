package com.luiz.transactions.dto;

import java.util.UUID;

public record UserResponseDTO(
    UUID id,
    String name,
    UUID accountId
) {}