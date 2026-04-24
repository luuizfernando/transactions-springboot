package com.luiz.transactions.ai;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.luiz.transactions.domain.transaction.enums.TransactionCategory;

@Component
public class PromptBuilder {
    
    public String buildClassificationPrompt(String description) {
        return """
            Classifique a transação abaixo em uma categoria:
            - ALIMENTACAO
            - TRANSPORTE
            - LAZER
            - MORADIA
            - OUTROS
            Transação: "%s"
            Responda apenas com a categoria.
            """.formatted(description);
    }

    public String buildSummaryPrompt(Map<TransactionCategory, BigDecimal> totals) {
        String categoriesText = totals.entrySet().stream()
                .map(e -> "- %s: %s".formatted(e.getKey(), e.getValue()))
                .collect(Collectors.joining("\n"));

        return """
            Analise os gastos mensais abaixo e gere um resumo financeiro curto e amigável em português:
            
            %s
            
            Gere um parágrafo curto com dicas ou observações sobre onde o dinheiro está sendo gasto.
            """.formatted(categoriesText);
    }

    public String buildRiskAnalysisPrompt(BigDecimal amount, BigDecimal average, String riskLevel, String description) {
        return """
            Analise uma tentativa de transação financeira:
            - Valor: R$ %s
            - Média histórica da conta: R$ %s
            - Nível de risco calculado: %s
            - Descrição: %s

            Explique em 1 ou 2 frases curtas em português (PT-BR) por que essa transação foi classificada com esse nível de risco. 
            Seja direto e mencione o valor e a média se relevante.
            """.formatted(amount, average != null ? average : "N/A", riskLevel, description != null ? description : "N/A");
    }

}