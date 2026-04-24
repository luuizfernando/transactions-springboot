package com.luiz.transactions.service;

import com.luiz.transactions.ai.AiClient;
import com.luiz.transactions.ai.PromptBuilder;
import com.luiz.transactions.domain.account.Account;
import com.luiz.transactions.domain.insight.dto.InsightSummaryResponseDTO;
import com.luiz.transactions.domain.transaction.Transaction;
import com.luiz.transactions.domain.transaction.enums.TransactionCategory;
import com.luiz.transactions.domain.user.User;
import com.luiz.transactions.exception.ResourceNotFoundException;
import com.luiz.transactions.repository.AccountRepository;
import com.luiz.transactions.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InsightService {

    private static final Logger log = LoggerFactory.getLogger(InsightService.class);

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final PromptBuilder promptBuilder;
    private final AiClient aiClient;

    public InsightService(
        AccountRepository accountRepository,
        TransactionRepository transactionRepository,
        PromptBuilder promptBuilder,
        AiClient aiClient
    ) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.promptBuilder = promptBuilder;
        this.aiClient = aiClient;
    }

    public InsightSummaryResponseDTO generateSummary(User user) {
        log.info("[Insight] Gerando resumo para o usuário {}", user.getId());

        Optional<Account> accountOpt = accountRepository.findByUserId(user.getId());
        if (accountOpt.isEmpty()) {
            log.error("[Insight] Não foi possível encontrar uma conta associada ao usuário {}", user.getId());
            throw new ResourceNotFoundException("Não foi possível encontrar uma conta associada ao seu usuário.");
        }

        List<Transaction> transactions = transactionRepository.findByAccountId(accountOpt.get().getId());
        if (transactions.isEmpty()) {
            log.warn("[Insight] Não foi possível encontrar transações para a conta {}", accountOpt.get().getId());
            return new InsightSummaryResponseDTO("Você ainda não possui transações para gerar um resumo.");
        }

        Map<TransactionCategory, BigDecimal> totals = transactions.stream()
                .collect(
                    Collectors.groupingBy(
                        t -> t.getCategory() != null ? t.getCategory() : TransactionCategory.OUTROS,
                        Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)
                    )
                );

        String prompt = promptBuilder.buildSummaryPrompt(totals);
        
        try {
            String summary = aiClient.sendSummaryPrompt(prompt);
            return new InsightSummaryResponseDTO(summary);
        } catch (Exception e) {
            log.warn("[Insight] IA indisponível, retornando fallback | erro={}", e.getMessage());
            return new InsightSummaryResponseDTO("Resumo financeiro temporariamente indisponível.");
        }
    }

}