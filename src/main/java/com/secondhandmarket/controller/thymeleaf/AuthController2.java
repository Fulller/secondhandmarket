package com.secondhandmarket.controller.thymeleaf;

import com.secondhandmarket.dto.auth.AuthLogOutRequest;
import com.secondhandmarket.dto.auth.AuthLoginRequest;
import com.secondhandmarket.dto.auth.AuthRegisterRequest;
import com.secondhandmarket.dto.auth.AuthResponse;
import com.secondhandmarket.exception.AppException;
import com.secondhandmarket.service.AuthService;
import com.secondhandmarket.service.EmailService;
import com.secondhandmarket.util.CodeUtil;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequiredArgsConstructor

public class AuthController2 {
    @Value("${app.clientReceiveTokensPath}")
    private String clientReceiveTokensPath;
    private final AuthService authService;
    private final EmailService emailService;
    private final CodeUtil<AuthRegisterRequest> codeUtil;
    private final CodeUtil<String> forgotPasswordCodeUtil;

    @Autowired
    private JwtDecoder jwtDecoder;

    @GetMapping("/sign-in")
    public String loginPage(Model model, Authentication authentication, HttpSession session) {
        if (authentication != null && session != null) {
            return "redirect:/dashboard";
        }
        model.addAttribute("authLoginRequest", new AuthLoginRequest());
        return "signin";
    }
}
