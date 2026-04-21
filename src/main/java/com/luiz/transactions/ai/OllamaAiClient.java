package com.luiz.transactions.ai;

import com.luiz.transactions.ai.dto.OllamaOptions;
import com.luiz.transactions.ai.dto.OllamaRequest;
import com.luiz.transactions.ai.dto.OllamaResponse;
import com.luiz.transactions.domain.transaction.enums.TransactionCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

import java.time.Duration;

@Component
@ConditionalOnProperty(name = "ai.client.type", havingValue = "ollama", matchIfMissing = true)
public class OllamaAiClient implements AiClient {

    private static final Logger log = LoggerFactory.getLogger(OllamaAiClient.class);

    private final WebClient webClient;

    @Value("${ai.ollama.model}")
    private String model;

    @Value("${ai.ollama.timeout-seconds:10}")
    private int timeoutSeconds;

    @Value("${ai.ollama.max-tokens:10}")
    private int maxTokens;

    public OllamaAiClient(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public String sendPrompt(String prompt) {
        OllamaRequest request = new OllamaRequest(
            model,
            prompt,
            false,
            new OllamaOptions(maxTokens, 0.0)
        );

        log.info("[AI] Enviando prompt para IA | modelo={}", model);

        try {
            String raw = webClient.post()
                    .uri("/api/generate")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(OllamaResponse.class)
                    .timeout(Duration.ofSeconds(timeoutSeconds))
                    .retryWhen(Retry.fixedDelay(2, Duration.ofSeconds(1)))
                    .map(OllamaResponse::response)
                    .block();

            String category = sanitize(raw);
            log.info("[IA] Resposta recebida | raw='{}' | categoria='{}'", raw, category);

            return category;

        } catch (Exception e) {
            log.error("[IA] Falha ao classificar transação | erro={}", e.getMessage());
            return "OUTROS";
        }
    }

    private String sanitize(String raw) {
        if (raw == null) return "OUTROS";

        String upper = raw.strip().toUpperCase();
        for (TransactionCategory category : TransactionCategory.values()) {
            if (upper.contains(category.getValue())) {
                return category.getValue();
            }
        }
        
        return "OUTROS";
    }
    
}