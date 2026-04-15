package com.luiz.transactions.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luiz.transactions.domain.user.dto.LoginRequestDTO;
import com.luiz.transactions.domain.user.dto.LoginResponseDTO;
import com.luiz.transactions.domain.user.dto.RegisterDTO;
import com.luiz.transactions.domain.user.dto.UserResponseDTO;
import com.luiz.transactions.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO data) {
        LoginResponseDTO response = userService.login(data);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody @Valid RegisterDTO data) {
        UserResponseDTO response = userService.register(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
}