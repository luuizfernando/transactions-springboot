package com.luiz.transactions.domain.transaction.dto;

import com.luiz.transactions.domain.transaction.enums.RiskLevel;

public record TransactionRiskResponseDTO(
    
    RiskLevel risk,
    String reason

) {}