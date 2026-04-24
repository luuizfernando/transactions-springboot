package com.luiz.transactions.ai;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "ai.client.type", havingValue = "mock")
public class MockAiClient implements AiClient {
    @Override
    public String sendPrompt(String prompt) {
        return "ALIMENTACAO";
    }

    @Override
    public String sendSummaryPrompt(String prompt) {
        return "Este é um resumo financeiro de teste gerado pelo MockAiClient.";
    }
}
