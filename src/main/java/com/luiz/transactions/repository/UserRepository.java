package com.luiz.transactions.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.luiz.transactions.domain.user.User;

public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByName(String name);

    UserDetails findByName(String name);

}