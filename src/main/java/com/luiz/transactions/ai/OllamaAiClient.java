package com.luiz.transactions.ai;

import com.luiz.transactions.ai.dto.OllamaOptions;
import com.luiz.transactions.ai.dto.OllamaRequest;
import com.luiz.transactions.ai.dto.OllamaResponse;
import com.luiz.transactions.domain.transaction.enums.TransactionCategory;
import com.luiz.transactions.exception.AiErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

@Component
@ConditionalOnProperty(name = "ai.client.type", havingValue = "ollama", matchIfMissing = true)
public class OllamaAiClient implements AiClient {

    private static final Logger log = LoggerFactory.getLogger(OllamaAiClient.class);

    private final WebClient webClient;

    @Value("${ai.ollama.model}")
    private String model;

    @Value("${ai.ollama.timeout-seconds:30}")
    private int timeoutSeconds;

    @Value("${ai.ollama.max-tokens}")
    private int maxTokens;

    @Value("${ai.ollama.summary-max-tokens}")
    private int summaryMaxTokens;

    public OllamaAiClient(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public String sendPrompt(String prompt) {
        String raw = callModel(prompt, maxTokens);
        String category = sanitize(raw);
        log.info("[IA] Resposta de classificação | raw='{}' | categoria='{}'", raw, category);
        return category;
    }

    @Override
    public String sendSummaryPrompt(String prompt) {
        log.info("[IA] Solicitando resumo financeiro...");
        return callModel(prompt, summaryMaxTokens);
    }

    private String callModel(String prompt, int tokens) {
        OllamaRequest request = new OllamaRequest(
            model,
            prompt,
            false,
            new OllamaOptions(tokens, 0.0)
        );

        try {
            return webClient.post()
                    .uri("/api/generate")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(OllamaResponse.class)
                    .timeout(Duration.ofSeconds(timeoutSeconds))
                    .retryWhen(Retry.fixedDelay(2, Duration.ofSeconds(1))
                            .filter(e -> !(e instanceof TimeoutException))
                            .doBeforeRetry(signal -> log.warn("[IA] Tentativa {} após erro: {}", signal.totalRetries() + 1, signal.failure().getMessage())))
                    .map(OllamaResponse::response)
                    .block();
        } catch (Exception e) {
            log.error("[IA] Falha na chamada ao Ollama | erro={}", e.getMessage());
            throw new AiErrorException("O serviço de IA está temporariamente indisponível. Tente novamente mais tarde.");
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