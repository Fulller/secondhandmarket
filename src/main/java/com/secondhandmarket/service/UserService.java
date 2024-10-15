package com.secondhandmarket.service;

import com.secondhandmarket.dto.user.UserResponse;
import com.secondhandmarket.dto.user.UserUpdateRequest;
import com.secondhandmarket.exception.AppException;
import com.secondhandmarket.mapper.UserMapper;
import com.secondhandmarket.model.User;
import com.secondhandmarket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponse updateUser(UserUpdateRequest request) {
        String id = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "User not found", "user-e-01"));

        userMapper.toUpdateUser(user, request);

        return userMapper.toResponseUser(userRepository.save(user));
    }
}
