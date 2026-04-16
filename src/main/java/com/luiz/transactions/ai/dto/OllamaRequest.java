package com.luiz.transactions.ai.dto;

public record OllamaRequest(
    
    String model,
    String prompt,
    boolean stream,
    OllamaOptions options

) {}