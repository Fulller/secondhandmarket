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
    ResponseEntity<ApiResponse<UserResponse>> updateUser(@RequestBody UserUpdateRequest request) {
        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .code("user-s-01")
                .message("User update information success")
                .data(userService.updateUser(request))
                .build();
        return ResponseEntity.ok(apiResponse);
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

    @GetMapping
    ResponseEntity<ApiResponse<UserResponse>> getUser() {
        ApiResponse<UserResponse> apiResponse = ApiResponse.<UserResponse>builder()
                .code("user-s-03")
                .message("Get user successfully")
                .data(userService.getUser())
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
