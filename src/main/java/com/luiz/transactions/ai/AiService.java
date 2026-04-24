package com.luiz.transactions.ai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.luiz.transactions.domain.transaction.enums.TransactionCategory;

@Service
public class AiService {

    private static final Logger log = LoggerFactory.getLogger(AiService.class);
    
    private final AiClient aiClient;
    private final PromptBuilder promptBuilder;

    public AiService(AiClient aiClient, PromptBuilder promptBuilder) {
        this.aiClient = aiClient;
        this.promptBuilder = promptBuilder;
    }

    @Cacheable(value = "ai-classification", key = "#description")
    public TransactionCategory classifyTransaction(String description) {
        try {
            String prompt = promptBuilder.buildClassificationPrompt(description);
            return TransactionCategory.valueOf(aiClient.sendPrompt(prompt));
        } catch (Exception e) {
            return TransactionCategory.OUTROS;
        }
    }

    public String explainRisk(String prompt) {
        log.info("[IA] Solicitando explicação de risco | prompt={}", prompt.substring(0, Math.min(prompt.length(), 50)) + "...");
        try {
            return aiClient.sendSummaryPrompt(prompt);
        } catch (Exception e) {
            log.warn("[IA] Falha ao explicar risco, usando fallback | erro={}", e.getMessage());
            return "Análise de risco indisponível no momento.";
        }
    }

}