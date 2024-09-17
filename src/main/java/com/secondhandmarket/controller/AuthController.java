package com.secondhandmarket.controller;

import com.secondhandmarket.dto.auth.*;
import com.secondhandmarket.service.AuthService;
import com.secondhandmarket.dto.api.ApiResponse;
import com.secondhandmarket.service.EmailService;
import com.secondhandmarket.util.CodeUtil;
import lombok.AccessLevel;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthController {
    AuthService authService;
    EmailService emailService;
    CodeUtil<AuthRegisterRequest> codeUtil;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody @Valid AuthRegisterRequest request) {
        authService.register(request);
        String verificationCode = UUID.randomUUID().toString();
        codeUtil.save(verificationCode, request, 1);
        emailService.sendEmailToVerifyRegister(request.getEmail(), verificationCode);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code("auth-s-01")
                .message("Request register successful, check your email")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/register/verify/{verificationCode}")
    public ResponseEntity<ApiResponse<AuthResponse>> verifyRegister(@PathVariable String verificationCode) {
        AuthRegisterRequest request =  codeUtil.get(verificationCode);
        AuthResponse response = authService.verifyRegister(request);
        codeUtil.remove(verificationCode);
        ApiResponse<AuthResponse> apiResponse = ApiResponse.<AuthResponse>builder()
                .data(response)
                .code("auth-s-02")
                .message("Register successful")
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody @Valid AuthLoginRequest request){
        AuthResponse authResponse = authService.login(request);
        ApiResponse<AuthResponse> apiResponse =  ApiResponse.<AuthResponse>builder()
                .data(authResponse)
                .code("auth-s-03")
                .message("Login successful")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@RequestBody @Valid AuthRefreshTokenRequest request){
        AuthResponse authResponse = authService.refreshToken(request);
        ApiResponse<AuthResponse> apiResponse =  ApiResponse.<AuthResponse>builder()
                .data(authResponse)
                .code("auth-s-04")
                .message("Refresh new access token successful")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logOut(@RequestBody @Valid AuthLogOutRequest request){
        authService.logOut(request);
        ApiResponse<Void> apiResponse =  ApiResponse.<Void>builder()
                .code("auth-s-05")
                .message("Log out successful")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
