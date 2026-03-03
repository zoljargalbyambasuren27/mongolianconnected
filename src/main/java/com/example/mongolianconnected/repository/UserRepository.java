package com.example.mongolianconnected.repository;

import com.example.mongolianconnected.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    // Email check
    boolean existsByEmail(String email);

    // Phone number check
    boolean existsByPhone(String phone);

    // Find by email or username (depends on login flow)
    Optional<User> findByEmail(String email);
}
