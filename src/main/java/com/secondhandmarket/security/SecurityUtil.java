package com.secondhandmarket.security;

import com.secondhandmarket.exception.AppException;
import com.secondhandmarket.model.User;
import com.secondhandmarket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtil {
    final private UserRepository userRepository;

    public String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
    public User getCurrentUser() {
        return userRepository
                .findById(this.getCurrentUserId())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "User not found", "user-e-01"));
    }
}
