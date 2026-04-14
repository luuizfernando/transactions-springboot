package com.luiz.transactions.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luiz.transactions.domain.user.User;
import com.luiz.transactions.domain.user.dto.CreateUserRequestDTO;
import com.luiz.transactions.domain.user.dto.UserResponseDTO;
import com.luiz.transactions.exception.ConflictException;
import com.luiz.transactions.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponseDTO create(CreateUserRequestDTO data) {
        if (userRepository.existsByName(data.name())) {
            throw new ConflictException("Usuário já existe.");
        }
        User user = new User(data.name());
        userRepository.save(user);
        return new UserResponseDTO(user.getId(), user.getName(), user.getAccount().getId());
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> listAll() {
        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private UserResponseDTO toResponse(User user) {
        return new UserResponseDTO(user.getId(), user.getName(), user.getAccount().getId());
    }

}