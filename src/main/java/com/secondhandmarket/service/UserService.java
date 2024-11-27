package com.secondhandmarket.service;

import com.secondhandmarket.dto.user.UserResponse;
import com.secondhandmarket.dto.user.UserSellerInProductDTO;
import com.secondhandmarket.dto.user.UserUpdateRequest;
import com.secondhandmarket.exception.AppException;
import com.secondhandmarket.mapper.UserMapper;
import com.secondhandmarket.model.Address;
import com.secondhandmarket.model.User;
import com.secondhandmarket.repository.AddressRepository;
import com.secondhandmarket.repository.UserRepository;
import com.secondhandmarket.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AddressRepository addressRepository;
    private final SecurityUtil securityUtil;

    public UserResponse updateUser(UserUpdateRequest request) {
        User user = securityUtil.getCurrentUser();
        userMapper.toUpdateUser(user, request);
        if (request.getAddressId() != null) {
            Address address = addressRepository.findById(request.getAddressId())
                    .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Address not found", "address-e-01"));
            user.setAddress(address);
        }
        return userMapper.toResponseUser(userRepository.save(user));
    }

    public UserSellerInProductDTO getUserById(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "User not found", "user-e-02"));

        return userMapper.toUserSellerInProductDto(user);
    }

    public UserResponse getUser() {
        User user = securityUtil.getCurrentUser();
        return userMapper.toResponseUser(user);
    }
}
