package com.secondhandmarket.controller;

import com.secondhandmarket.dto.api.ApiResponse;
import com.secondhandmarket.dto.user.UserResponse;
import com.secondhandmarket.dto.user.UserSellerInProductDTO;
import com.secondhandmarket.dto.user.UserUpdateRequest;
import com.secondhandmarket.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @PutMapping
    ApiResponse<Object> updateUser(@RequestBody UserUpdateRequest request) {
        return ApiResponse.builder()
                .code("user-s-01")
                .message("User update information success")
                .data(userService.updateUser(request))
                .build();
    }
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserSellerInProductDTO>> getUserById(@PathVariable String userId) {
        ApiResponse<UserSellerInProductDTO> apiResponse = ApiResponse.<UserSellerInProductDTO>builder()
                .code("user-s-02")
                .message("User retrieved successfully")
                .data(userService.getUserById(userId))
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
