package com.secondhandmarket.controller;

import com.secondhandmarket.dto.auth.*;
import com.secondhandmarket.service.AuthService;
import com.secondhandmarket.dto.api.ApiResponse;
import com.secondhandmarket.service.EmailService;
import com.secondhandmarket.util.CodeUtil;
import com.secondhandmarket.util.CommonUtil;
import com.secondhandmarket.util.jwt.BaseJWTUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    @Value("${app.clientReceiveTokensPath}")
    private String clientReceiveTokensPath;
    private final AuthService authService;
    private final EmailService emailService;
    private final CodeUtil<AuthRegisterRequest> codeUtil;
    private final CodeUtil<String> forgotPasswordCodeUtil;
    private final SecurityUtil securityUtil;


    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody @Valid AuthRegisterRequest request) {
        authService.register(request);
        String verificationCode = UUID.randomUUID().toString();
        codeUtil.save(verificationCode, request, 1);
        emailService.sendEmailToVerifyRegister(request.getEmail(), verificationCode);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code("auth-s-01")
                .message("Request register successfully, check your email")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/register/verify/{verificationCode}")
    public RedirectView verifyRegister(@PathVariable String verificationCode) {
        AuthRegisterRequest request =  codeUtil.get(verificationCode);
        AuthResponse authResponse = authService.verifyRegister(request);
        codeUtil.remove(verificationCode);
        emailService.sendEmailToWelcome(request.getEmail());
        String redirectUrl = UriComponentsBuilder.fromUriString(clientReceiveTokensPath)
                .queryParam("accessToken", authResponse.getAccessToken())
                .queryParam("refreshToken", authResponse.getRefreshToken())
                .toUriString();
        return new RedirectView(redirectUrl);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody @Valid AuthLoginRequest request){
        AuthResponse authResponse = authService.login(request);
        ApiResponse<AuthResponse> apiResponse =  ApiResponse.<AuthResponse>builder()
                .data(authResponse)
                .code("auth-s-03")
                .message("Login successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@RequestBody @Valid AuthRefreshTokenRequest request){
        AuthResponse authResponse = authService.refreshToken(request);
        ApiResponse<AuthResponse> apiResponse =  ApiResponse.<AuthResponse>builder()
                .data(authResponse)
                .code("auth-s-04")
                .message("Refresh new access token successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logOut(@RequestBody @Valid AuthLogOutRequest request){
        authService.logOut(request);
        ApiResponse<Void> apiResponse =  ApiResponse.<Void>builder()
                .code("auth-s-05")
                .message("Log out successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> logOut(@RequestBody @Valid AuthChangePasswordRequest request){
        String userId = BaseJWTUtil.getPayload(SecurityContextHolder.getContext()).getId();
        authService.changePassword(userId, request);
        ApiResponse<Void> apiResponse =  ApiResponse.<Void>builder()
                .code("auth-s-06")
                .message("Password changed successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/login/oauth2/success")
    public RedirectView loginOAuth2Success(@AuthenticationPrincipal OAuth2User oAuth2User) {
        AuthResponse authResponse = authService.loginOAuth2Success(oAuth2User);
        String redirectUrl = UriComponentsBuilder.fromUriString(clientReceiveTokensPath)
                .queryParam("accessToken", authResponse.getAccessToken())
                .queryParam("refreshToken", authResponse.getRefreshToken())
                .toUriString();
        return new RedirectView(redirectUrl);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forGotPassword(@RequestBody AuthForgotPasswordRequest request) {
        authService.forgotPassword(request);
        String verificationCode = CommonUtil.generateVerificationCode();
        forgotPasswordCodeUtil.save(CommonUtil.getForgotPasswordKey(verificationCode), request.getEmail(), 1);
        emailService.sendEmailToVerifyForgotPassword(request.getEmail(), verificationCode);
        ApiResponse<Void> apiResponse =  ApiResponse.<Void>builder()
                .code("auth-s-08")
                .message("Request to get new password successfully, please check your email")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PostMapping("/forgot-password/verify")
    public ResponseEntity<ApiResponse<AuthResponse>> verifyForgotPassword(@RequestBody @Valid AuthVerifyForgotPasswordRequest request) {
        String email = forgotPasswordCodeUtil.get(CommonUtil.getForgotPasswordKey(request.getCode()));
        AuthResponse authResponse =  authService.verifyForgotPassword(email, request);
        ApiResponse<AuthResponse> apiResponse =  ApiResponse.<AuthResponse>builder()
                .data(authResponse)
                .code("auth-s-09")
                .message("Verify forgot password successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/info")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<AuthUserInfoResponse>> getInfo() {
        String userId = securityUtil.getCurrentUserId();
        ApiResponse<AuthUserInfoResponse> apiResponse =  ApiResponse.<AuthUserInfoResponse>builder()
                .data(authService.getUserInfo(userId))
                .code("auth-s-10")
                .message("Get user info successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}

