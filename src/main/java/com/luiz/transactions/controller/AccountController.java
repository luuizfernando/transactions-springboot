package com.luiz.transactions.controller;

import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luiz.transactions.domain.account.dto.AccountResponseDTO;
import com.luiz.transactions.domain.account.dto.BalanceResponseDTO;
import com.luiz.transactions.domain.account.dto.CreateAccountRequestDTO;
import com.luiz.transactions.domain.transaction.dto.TransactionResponseDTO;
import com.luiz.transactions.domain.user.User;
import com.luiz.transactions.repository.AccountRepository;
import com.luiz.transactions.service.AccountService;
import com.luiz.transactions.service.TransactionService;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Accounts", description = "Endpoints para criação de conta, consulta de saldo e extrato")
public class AccountController {

    private final TransactionService transactionService;
    private final AccountRepository accountRepository;
    private final AccountService accountService;

    public AccountController(
        TransactionService transactionService,
        AccountRepository accountRepository,
        AccountService accountService
    ) {
        this.transactionService = transactionService;
        this.accountRepository = accountRepository;
        this.accountService = accountService;
    }

    @Operation(summary = "Cria uma conta", description = "Cria uma conta com UUID próprio vinculada ao usuário autenticado")
    @RequestBody(
        description = "Opcional; pode ser omitido ou enviado como objeto vazio {}",
        required = false,
        content = @Content(schema = @Schema(implementation = CreateAccountRequestDTO.class))
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Conta criada com sucesso"),
        @ApiResponse(responseCode = "409", description = "Usuário já possui conta")
    })
    @PostMapping
    public ResponseEntity<AccountResponseDTO> createAccount(@AuthenticationPrincipal User user) {
        AccountResponseDTO response = accountService.createAccountForUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Consulta o saldo da conta", description = "Retorna o saldo atual de uma conta específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Saldo retornado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    @GetMapping("/{id}/balance")
    public ResponseEntity<BalanceResponseDTO> getBalance(
        @PathVariable UUID id,
        @AuthenticationPrincipal User user
    ) {
        assertAccountAccess(id, user);
        BalanceResponseDTO response = transactionService.getBalance(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Lista transações da conta", description = "Retorna o histórico de transações de uma conta específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transações retornadas com sucesso"),
        @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<TransactionResponseDTO>> listTransactions(
        @PathVariable UUID id,
        @AuthenticationPrincipal User user
    ) {
        assertAccountAccess(id, user);
        List<TransactionResponseDTO> response = transactionService.listTransactions(id);
        return ResponseEntity.ok(response);
    }

    private void assertAccountAccess(UUID id, User user) {
        if (user.getAuthorities().stream().anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()))) {
            return;
        }
        
        if (!accountRepository.existsByIdAndUser(id, user)) {
            throw new AccessDeniedException("Acesso negado a esta conta.");
        }
    }

}