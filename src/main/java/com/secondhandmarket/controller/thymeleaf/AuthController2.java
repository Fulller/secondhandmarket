package com.secondhandmarket.controller.thymeleaf;

import com.secondhandmarket.dto.auth.AuthLogOutRequest;
import com.secondhandmarket.dto.auth.AuthLoginRequest;
import com.secondhandmarket.dto.auth.AuthRegisterRequest;
import com.secondhandmarket.dto.auth.AuthResponse;
import com.secondhandmarket.exception.AppException;
import com.secondhandmarket.service.AuthService;
import com.secondhandmarket.service.EmailService;
import com.secondhandmarket.util.CodeUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequiredArgsConstructor

public class AuthController2 {
    @Value("${app.clientReceiveTokensPath}")
    private String clientReceiveTokensPath;
    private final AuthService authService;
    private final EmailService emailService;
    private final CodeUtil<AuthRegisterRequest> codeUtil;
    private final CodeUtil<String> forgotPasswordCodeUtil;

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    @Autowired
    private JwtDecoder jwtDecoder;

    @GetMapping("/sign-in")
    public String showLoginPage(Model model) {
        model.addAttribute("authLoginRequest", new AuthLoginRequest());
        return "signin";
    }

    @PostMapping("/signin")
    public String login(@ModelAttribute("authLoginRequest") AuthLoginRequest request, Model model) {
        try {
            AuthResponse authResponse = authService.login(request);
            String accessToken = authResponse.getAccessToken();
            Jwt jwt = jwtDecoder.decode(accessToken);
            String scope = jwt.getClaimAsString("scope");
            if (scope.contains("ROLE_ADMIN")) {
                model.addAttribute("accessToken", accessToken);
                return "redirect:/dashboard";
            } else {
                model.addAttribute("message", "Đăng nhập thành công nhưng không có quyền Admin");
                return "signin";
            }
        } catch (AppException ex) {
            model.addAttribute("error", ex.getMessage());
            return "signin";
        }
    }

    @PostMapping("/log-out")
    public String logOut(@RequestBody @Valid AuthLogOutRequest request){
        authService.logOut(request);
        return "redirect:/sign-in";
    }
}
