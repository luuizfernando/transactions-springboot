package com.luiz.transactions.controller;

import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luiz.transactions.domain.account.dto.BalanceResponseDTO;
import com.luiz.transactions.domain.transaction.dto.TransactionResponseDTO;
import com.luiz.transactions.service.TransactionService;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Accounts", description = "Endpoints para consulta de saldo e extrato de contas")
public class AccountController {

    private final TransactionService transactionService;

    public AccountController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(summary = "Consulta o saldo da conta", description = "Retorna o saldo atual de uma conta específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Saldo retornado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    @GetMapping("/{id}/balance")
    public ResponseEntity<BalanceResponseDTO> getBalance(@PathVariable UUID id) {
        BalanceResponseDTO response = transactionService.getBalance(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Lista transações da conta", description = "Retorna o histórico de transações de uma conta específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transações retornadas com sucesso"),
        @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<TransactionResponseDTO>> listTransactions(@PathVariable UUID id) {
        List<TransactionResponseDTO> response = transactionService.listTransactions(id);
        return ResponseEntity.ok(response);
    }

}