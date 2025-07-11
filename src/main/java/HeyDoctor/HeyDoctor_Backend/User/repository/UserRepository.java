package com.example.signup.repository;

import com.example.signup.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndPassword(String email, String password);
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmailAndPassword(String email, String password);
}
