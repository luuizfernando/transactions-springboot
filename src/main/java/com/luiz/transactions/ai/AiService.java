package com.luiz.transactions.ai;

import org.springframework.stereotype.Service;

import com.luiz.transactions.domain.transaction.enums.TransactionCategory;

@Service
public class AiService {
    
    private final AiClient aiClient;
    private final PromptBuilder promptBuilder;

    public AiService(AiClient aiClient, PromptBuilder promptBuilder) {
        this.aiClient = aiClient;
        this.promptBuilder = promptBuilder;
    }

    public TransactionCategory classifyTransaction(String description) {
        String prompt = promptBuilder.buildClassificationPrompt(description);
        return TransactionCategory.valueOf(aiClient.sendPrompt(prompt));
    }

}