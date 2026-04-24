package com.luiz.transactions.ai;

public interface AiClient {
    
    String sendPrompt(String prompt);
    String sendSummaryPrompt(String prompt);

}