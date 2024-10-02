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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String showLoginPage(Model model) {
        model.addAttribute("authLoginRequest", new AuthLoginRequest());
        return "signin";
    }

    @PostMapping("/signin")
    public String login(@ModelAttribute("authLoginRequest") AuthLoginRequest request, Model model, RedirectAttributes redirectAttributes) {
        try {
            AuthResponse authResponse = authService.login(request);
            String accessToken = authResponse.getAccessToken();
            redirectAttributes.addAttribute("accessToken", accessToken);
            return "redirect:/auth/receive-token";
        } catch (AppException ex) {
            model.addAttribute("error", ex.getMessage());
            return "signin";
        }
    }

    @GetMapping("auth/receive-token")
    public String receiveToken(@RequestParam("accessToken") String accessToken, Model model) {
        Jwt jwt = jwtDecoder.decode(accessToken);
        String role = jwt.getClaim("scope");
        model.addAttribute("accessToken", accessToken);
//        if (role.contains("ROLE_ADMIN")) {
//            return "redirect:/dashboard";
//        }
        return "receive-token";
    }

    @PostMapping("/log-out")
    public String logOut(@RequestBody @Valid AuthLogOutRequest request){
        authService.logOut(request);
        return "redirect:/sign-in";
    }
}
