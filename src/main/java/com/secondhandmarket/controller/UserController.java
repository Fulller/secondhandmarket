package com.secondhandmarket.controller;

import com.secondhandmarket.dto.api.ApiResponse;
import com.secondhandmarket.dto.user.UserUpdateRequest;
import com.secondhandmarket.service.UserService;
import lombok.RequiredArgsConstructor;
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


}
