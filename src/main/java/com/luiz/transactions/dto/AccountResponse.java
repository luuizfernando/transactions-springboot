package com.luiz.transactions.dto;

import java.util.UUID;

public record AccountResponse(
    UUID id,
    UUID userId
) {}