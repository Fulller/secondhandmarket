package com.secondhandmarket.controller.thymeleaf;


import com.secondhandmarket.model.User;
import com.secondhandmarket.repository.UserRepository;
import com.secondhandmarket.service.thymeleaf.UserDetailsImpl;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequiredArgsConstructor
public class DashboardController {
    private final UserRepository userRepository;

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public String dashboard( HttpSession session){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            String userId = userDetails.getId();
            User user = userRepository.findById(userId).orElse(null);
            session.setAttribute("user", user);
        }
        return "layout";
    }
}


