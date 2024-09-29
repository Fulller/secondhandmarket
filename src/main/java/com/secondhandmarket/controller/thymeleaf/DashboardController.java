package com.secondhandmarket.controller.thymeleaf;


import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequiredArgsConstructor
public class DashboardController {
    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);
    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String showDashboard() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        logger.info("Phương thức showDashboard đã được gọi với quyền: " + auth.getAuthorities());
        return "/dashboard/dashboard";
    }
}


