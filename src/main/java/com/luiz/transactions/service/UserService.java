package com.luiz.transactions.service;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luiz.transactions.config.security.TokenService;
import com.luiz.transactions.domain.user.User;
import com.luiz.transactions.domain.user.dto.LoginRequestDTO;
import com.luiz.transactions.domain.user.dto.LoginResponseDTO;
import com.luiz.transactions.domain.user.dto.RegisterDTO;
import com.luiz.transactions.domain.user.dto.UserResponseDTO;
import com.luiz.transactions.domain.user.enums.UserRole;
import com.luiz.transactions.exception.ConflictException;
import com.luiz.transactions.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final AccountService accountService;

    public UserService(UserRepository userRepository, AuthenticationManager authenticationManager, TokenService tokenService, AccountService accountService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.accountService = accountService;
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> listAll() {
        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private UserResponseDTO toResponse(User user) {
        var account = user.getAccount();
        return new UserResponseDTO(
            user.getId(),
            user.getName(),
            account != null ? account.getId() : null
        );
    }

    @Transactional
    public UserResponseDTO register(RegisterDTO data) {
        if (userRepository.existsByName(data.name())) {
            throw new ConflictException("Usuário já existe.");
        }
        
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User user = new User(data.name(), encryptedPassword, UserRole.USER);
        userRepository.save(user);
        
        accountService.createAccountForUser(user);
        
        return toResponse(user);
    }

    public LoginResponseDTO login(LoginRequestDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.name(), data.password());
        var authentication = authenticationManager.authenticate(usernamePassword);

        User user = (User) authentication.getPrincipal();
        var token = tokenService.generateToken(user);
        return new LoginResponseDTO(token);
    }

}