package com.secondhandmarket.repository;

import com.secondhandmarket.model.RefreshToken;
import com.secondhandmarket.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    Optional<RefreshToken> findByUserId(String userId);
    Optional<RefreshToken> findByUser(User user);
}
