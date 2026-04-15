package com.luiz.transactions.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luiz.transactions.domain.transaction.dto.DepositRequestDTO;
import com.luiz.transactions.domain.transaction.dto.TransactionResponseDTO;
import com.luiz.transactions.domain.transaction.dto.TransferRequestDTO;
import com.luiz.transactions.service.TransactionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Transactions", description = "Endpoints para realização de depósitos e transferências")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(summary = "Realiza um depósito", description = "Adiciona saldo a uma conta")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Depósito realizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados de depósito inválidos"),
        @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponseDTO> deposit(
        @Valid @RequestBody DepositRequestDTO data,
        @Parameter(description = "Chave de idempotência para evitar duplicidade", example = "unique-key-123")
        @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey
    ) {
        TransactionResponseDTO response = transactionService.deposit(data, idempotencyKey);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Realiza uma transferência", description = "Transfere saldo entre duas contas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Transferência realizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Saldo insuficiente ou dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Conta de origem ou destino não encontrada")
    })
    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponseDTO> transfer(
        @Valid @RequestBody TransferRequestDTO data,
        @Parameter(description = "Chave de idempotência para evitar duplicidade", example = "unique-key-456")
        @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey
    ) {
        TransactionResponseDTO response = transactionService.transfer(data, idempotencyKey);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}