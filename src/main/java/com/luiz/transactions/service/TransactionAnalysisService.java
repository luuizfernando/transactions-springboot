package com.luiz.transactions.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.luiz.transactions.ai.AiService;
import com.luiz.transactions.ai.PromptBuilder;
import com.luiz.transactions.domain.transaction.dto.TransactionAnalyzeRequestDTO;
import com.luiz.transactions.domain.transaction.dto.TransactionRiskResponseDTO;
import com.luiz.transactions.domain.transaction.enums.RiskLevel;
import com.luiz.transactions.repository.TransactionRepository;

@Service
public class TransactionAnalysisService {

    private static final Logger log = LoggerFactory.getLogger(TransactionAnalysisService.class);

    private final TransactionRepository transactionRepository;
    private final AiService aiService;
    private final PromptBuilder promptBuilder;

    public TransactionAnalysisService(TransactionRepository transactionRepository, AiService aiService, PromptBuilder promptBuilder) {
        this.transactionRepository = transactionRepository;
        this.aiService = aiService;
        this.promptBuilder = promptBuilder;
    }

    public TransactionRiskResponseDTO analyze(TransactionAnalyzeRequestDTO data) {
        Optional<BigDecimal> averageOpt = transactionRepository.findAverageAmountByAccountId(data.accountId());
        BigDecimal average = averageOpt.orElse(BigDecimal.ZERO);
        BigDecimal amount = data.amount();

        RiskLevel riskLevel = calculateRiskLevel(amount, average, averageOpt.isEmpty());
        String prompt = promptBuilder.buildRiskAnalysisPrompt(amount, average, riskLevel.name(), data.description());

        log.info("[Analise] Solicitando explicação de risco | accountId={}, riskLevel={}", data.accountId(), riskLevel);
        String reason = aiService.explainRisk(prompt);

        return new TransactionRiskResponseDTO(riskLevel, reason);
    }

    private RiskLevel calculateRiskLevel(BigDecimal amount, BigDecimal average, boolean noHistory) {
        if (noHistory) {
            if (amount.compareTo(new BigDecimal("5000")) > 0) return RiskLevel.HIGH;
            if (amount.compareTo(new BigDecimal("1000")) > 0) return RiskLevel.MEDIUM;
            return RiskLevel.LOW;
        }

        BigDecimal threeTimesAverage = average.multiply(new BigDecimal("3"));
        BigDecimal oneAndHalfTimesAverage = average.multiply(new BigDecimal("1.5"));

        if (amount.compareTo(threeTimesAverage) > 0) return RiskLevel.HIGH;
        if (amount.compareTo(oneAndHalfTimesAverage) > 0) return RiskLevel.MEDIUM;
        return RiskLevel.LOW;
    }
    
}