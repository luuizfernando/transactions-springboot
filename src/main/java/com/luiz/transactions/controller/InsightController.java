package com.luiz.transactions.controller;

import com.luiz.transactions.domain.insight.dto.InsightSummaryResponseDTO;
import com.luiz.transactions.domain.user.User;
import com.luiz.transactions.service.InsightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/insights")
@Tag(name = "Insights", description = "Endpoints para geração de resumos e análises financeiras com IA")
public class InsightController {

    private final InsightService insightService;

    public InsightController(InsightService insightService) {
        this.insightService = insightService;
    }

    @Operation(summary = "Gera um resumo financeiro", description = "Analisa as transações do usuário e gera um resumo utilizando IA")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Resumo gerado com sucesso"),
        @ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
        @ApiResponse(responseCode = "404", description = "Conta ou transações não encontradas")
    })
    @GetMapping("/summary")
    public ResponseEntity<InsightSummaryResponseDTO> getSummary(@AuthenticationPrincipal User user) {
        InsightSummaryResponseDTO response = insightService.generateSummary(user);
        return ResponseEntity.ok(response);
    }

}