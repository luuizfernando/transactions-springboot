package com.luiz.transactions.ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OllamaOptions(

    @JsonProperty("num_predict")
    int numPredict,
    
    double temperature
    
) {}