package com.evaluacion2023.repository;

import com.evaluacion2023.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    Optional<User> findById(UUID userId);
}
