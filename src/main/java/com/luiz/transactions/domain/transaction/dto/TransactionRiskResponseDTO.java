package com.luiz.transactions.domain.transaction.dto;

import com.luiz.transactions.domain.transaction.enums.RiskLevel;
import io.swagger.v3.oas.annotations.media.Schema;

public record TransactionRiskResponseDTO(
    
    @Schema(description = "Nível de risco calculado", example = "LOW")
    RiskLevel risk,

    @Schema(description = "Explicação detalhada gerada pela IA sobre o risco", example = "A transação está dentro do padrão...")
    String reason

) {}