package com.luiz.transactions.domain.user.dto;

import java.util.UUID;

public record UserResponseDTO(
    UUID id,
    String name,
    UUID accountId
) {}