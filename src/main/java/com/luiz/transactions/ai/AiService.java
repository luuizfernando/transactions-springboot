package com.luiz.transactions.ai;

import org.springframework.stereotype.Service;

@Service
public class AiService {
    private final AiClient aiClient;
    private final PromptBuilder promptBuilder;

    public AiService(AiClient aiClient, PromptBuilder promptBuilder) {
        this.aiClient = aiClient;
        this.promptBuilder = promptBuilder;
    }

    public String classifyTransaction(String description) {
        String prompt = promptBuilder.buildClassificationPrompt(description);
        return aiClient.sendPrompt(prompt);
    }
}