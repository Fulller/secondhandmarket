package com.secondhandmarket.controller;

import com.secondhandmarket.dto.api.ApiResponse;
import com.secondhandmarket.dto.request.AddressCreationRequest;
import com.secondhandmarket.dto.request.AddressUpdateRequest;
import com.secondhandmarket.dto.request.UserUpdateRequest;
import com.secondhandmarket.dto.response.UserResponse;
import com.secondhandmarket.repository.UserRepository;
import com.secondhandmarket.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PutMapping("users")
    ApiResponse<Object> updateUser(@RequestBody UserUpdateRequest request) {
        return ApiResponse.builder()
                .code("abc-123")
                .message("User update information success")
                .data(userService.updateUser(request))
                .build();
    }

    @PostMapping("/createAddress")
    ApiResponse<Object> createAddress(@RequestBody AddressCreationRequest request) {
        return ApiResponse.builder()
                .code("abc-123")
                .message("User create address success")
                .data(userService.createAddress(request))
                .build();
    }

    @PutMapping("/addresses")
    ApiResponse<Object> updateUserAddress(@RequestBody AddressUpdateRequest request) {
        return ApiResponse.builder()
                .code("abc-123")
                .message("User update address success")
                .data(userService.userUpdateAddress(request))
                .build();
    }

}
