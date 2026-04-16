package com.luiz.transactions.ai;

import org.springframework.stereotype.Component;

@Component
public class MockAiClient implements AiClient {
    @Override
    public String sendPrompt(String prompt) {
        return "ALIMENTACAO";
    }
}