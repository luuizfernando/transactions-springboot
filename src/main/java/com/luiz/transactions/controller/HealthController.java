package com.luiz.transactions.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
@Tag(name = "Health Check", description = "Endpoint para verificar o estado da aplicação")
public class HealthController {

    @Operation(summary = "Verifica se a aplicação está no ar", description = "Retorna OK se a aplicação estiver respondendo")
    @ApiResponse(responseCode = "200", description = "Aplicação está no ar")
    @GetMapping
    public ResponseEntity<String> health() {
        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }
    
}