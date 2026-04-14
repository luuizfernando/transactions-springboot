package com.luiz.transactions.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luiz.transactions.dto.BalanceResponseDTO;
import com.luiz.transactions.dto.TransactionResponseDTO;
import com.luiz.transactions.service.TransactionService;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final TransactionService transactionService;

    public AccountController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<BalanceResponseDTO> getBalance(@PathVariable UUID id) {
        BalanceResponseDTO response = transactionService.getBalance(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<TransactionResponseDTO>> listTransactions(@PathVariable UUID id) {
        List<TransactionResponseDTO> response = transactionService.listTransactions(id);
        return ResponseEntity.ok(response);
    }

}