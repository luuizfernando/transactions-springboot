package com.luiz.transactions.controller;

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
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponseDTO> deposit(
        @Valid @RequestBody DepositRequestDTO data,
        @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey
    ) {
        TransactionResponseDTO response = transactionService.deposit(data, idempotencyKey);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponseDTO> transfer(
        @Valid @RequestBody TransferRequestDTO data,
        @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey
    ) {
        TransactionResponseDTO response = transactionService.transfer(data, idempotencyKey);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}