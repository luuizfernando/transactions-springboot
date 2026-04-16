package com.luiz.transactions.ai;

import org.springframework.stereotype.Component;

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
}