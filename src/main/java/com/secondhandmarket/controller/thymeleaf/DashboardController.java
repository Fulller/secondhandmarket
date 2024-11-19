package com.secondhandmarket.controller.thymeleaf;


import com.secondhandmarket.mapper.UserMapper;
import com.secondhandmarket.model.User;
import com.secondhandmarket.repository.UserRepository;
import com.secondhandmarket.service.thymeleaf.UserDetailsImpl;
import com.secondhandmarket.util.jwt.AccessTokenUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequiredArgsConstructor
public class DashboardController {
    private final UserRepository userRepository;
    private  final UserMapper userMapper;
    private final AccessTokenUtil accessTokenUtil;

    @GetMapping("/receive-token")
    public String receiveToken(HttpSession session, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            String userId = userDetails.getId();
            User user = userRepository.findById(userId).orElse(null);
            if (user != null) {
                String accessTokenString = accessTokenUtil.generateToken(userMapper.toJWTPayloadDto(user));
                model.addAttribute("accessToken", accessTokenString);
            }
            session.setAttribute("user", user);
        }
        return "receive-token";
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public String dashboard(){
        return "layout";
    }
}


