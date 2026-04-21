package com.luiz.transactions.domain.insight.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record InsightSummaryResponseDTO(

    @Schema(description = "Resumo financeiro gerado pela IA", example = "Você está gastando muito em alimentação. Considere buscar opções mais econômicas.")
    String summary

){}