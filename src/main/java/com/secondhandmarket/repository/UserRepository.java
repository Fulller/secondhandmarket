package com.secondhandmarket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.secondhandmarket.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
