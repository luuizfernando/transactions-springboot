package com.luiz.transactions.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luiz.transactions.domain.user.dto.UserResponseDTO;
import com.luiz.transactions.service.UserService;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Endpoints para listagem de usuários (requer perfil ADMIN)")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Lista todos os usuários", description = "Retorna uma lista com todos os usuários cadastrados e suas contas vinculadas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
        @ApiResponse(responseCode = "403", description = "Acesso negado para usuários não-ADMIN")
    })
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> list() {
        List<UserResponseDTO> users = userService.listAll();
        return ResponseEntity.ok(users);
    }
    
}