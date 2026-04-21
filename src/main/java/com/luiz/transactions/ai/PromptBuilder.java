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
}