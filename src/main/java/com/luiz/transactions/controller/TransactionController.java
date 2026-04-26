package com.luiz.transactions.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.UUID;
import com.luiz.transactions.domain.user.User;
import com.luiz.transactions.domain.user.enums.UserRole;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luiz.transactions.domain.transaction.dto.DepositRequestDTO;
import com.luiz.transactions.domain.transaction.dto.TransactionAnalyzeRequestDTO;
import com.luiz.transactions.domain.transaction.dto.TransactionRiskResponseDTO;
import com.luiz.transactions.domain.transaction.dto.TransactionResponseDTO;
import com.luiz.transactions.domain.transaction.dto.TransferRequestDTO;
import com.luiz.transactions.service.TransactionAnalysisService;
import com.luiz.transactions.service.TransactionService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

@RestController
@RequestMapping("/api/transactions")
@Validated
@Tag(name = "Transactions", description = "Endpoints para realização de depósitos e transferências")
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionAnalysisService transactionAnalysisService;

    public TransactionController(TransactionService transactionService, TransactionAnalysisService transactionAnalysisService) {
        this.transactionService = transactionService;
        this.transactionAnalysisService = transactionAnalysisService;
    }

    @Operation(summary = "Realiza um depósito", description = "Adiciona saldo a uma conta")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Depósito realizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados de depósito inválidos"),
        @ApiResponse(responseCode = "403", description = "Acesso negado"),
        @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponseDTO> deposit(
        @Valid @RequestBody DepositRequestDTO data,
        @AuthenticationPrincipal User user,
        @Parameter(description = "Chave de idempotência para evitar duplicidade", example = "unique-key-123")
        @RequestHeader(value = "Idempotency-Key", required = false) @Size(max = 255, message = "A chave de idempotência deve ter no máximo 255 caracteres") String idempotencyKey
    ) {
        validateOwnership(user, data.accountId());
        TransactionResponseDTO response = transactionService.deposit(data, idempotencyKey);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Realiza uma transferência", description = "Transfere saldo entre duas contas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Transferência realizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Saldo insuficiente ou dados inválidos"),
        @ApiResponse(responseCode = "403", description = "Acesso negado"),
        @ApiResponse(responseCode = "404", description = "Conta de origem ou destino não encontrada")
    })
    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponseDTO> transfer(
        @Valid @RequestBody TransferRequestDTO data,
        @AuthenticationPrincipal User user,
        @Parameter(description = "Chave de idempotência para evitar duplicidade", example = "unique-key-456")
        @RequestHeader(value = "Idempotency-Key", required = false) @Size(max = 255, message = "A chave de idempotência deve ter no máximo 255 caracteres") String idempotencyKey
    ) {
        validateOwnership(user, data.fromAccountId());
        TransactionResponseDTO response = transactionService.transfer(data, idempotencyKey);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Analisa o risco de uma transação", description = "Avalia se uma transação é suspeita com base no histórico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Análise realizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados de análise inválidos"),
        @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PostMapping("/analyze")
    public ResponseEntity<TransactionRiskResponseDTO> analyze(
        @Valid @RequestBody TransactionAnalyzeRequestDTO data,
        @AuthenticationPrincipal User user
    ) {
        validateOwnership(user, data.accountId());
        TransactionRiskResponseDTO response = transactionAnalysisService.analyze(data);
        return ResponseEntity.ok(response);
    }

    private void validateOwnership(User user, UUID accountId) {
        if (user.getRole() != UserRole.ADMIN && (user.getAccount() == null || !user.getAccount().getId().equals(accountId))) {
            throw new AccessDeniedException("Você não tem permissão para realizar esta operação nesta conta.");
        }
    }

}