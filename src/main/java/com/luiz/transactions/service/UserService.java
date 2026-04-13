package com.luiz.transactions.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luiz.transactions.domain.user.User;
import com.luiz.transactions.dto.CreateUserRequest;
import com.luiz.transactions.dto.UserResponse;
import com.luiz.transactions.exception.ResourceNotFoundException;
import com.luiz.transactions.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponse create(CreateUserRequest data) {
        if (userRepository.existsByName(data.name())) {
            throw new ResourceNotFoundException("Usuário já existe.");
        }
        User user = new User();
        user.setName(data.name());
        userRepository.save(user);
        return new UserResponse(user.getId(), user.getName());
    }

}