package com.secondhandmarket.controller.thymeleaf;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequiredArgsConstructor
public class DashboardController {
    @GetMapping("/dashboard")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String showDashboard() {
        return "layout";
    }
}


