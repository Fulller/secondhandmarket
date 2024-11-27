package com.secondhandmarket.config;

import com.secondhandmarket.enums.ERole;
import com.secondhandmarket.model.User;
import com.secondhandmarket.repository.UserRepository;
import com.secondhandmarket.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EnumSet;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class InitAppConfig {
    @Value("${app.admin.email}")
    private String ADMIN_EMAIL;

    @Value("${app.admin.password}")
    private String ADMIN_PASSWORD;

    private final UserRepository userRepository;
    private final PasswordUtil passwordUtil;

    @Bean
    ApplicationRunner applicationRunner() {
        return args -> {
            boolean isExistedAdmin = userRepository.existsByEmailAndIsFromOutsideFalse(ADMIN_EMAIL);
            if (isExistedAdmin) return;
            Set<ERole> roles = EnumSet.allOf(ERole.class);
            User admin = User.builder()
                    .email(ADMIN_EMAIL)
                    .name("Root Admin")
                    .password(passwordUtil.encodePassword(ADMIN_PASSWORD))
                    .isFromOutside(false)
                    .roles(roles)
                    .build();
            userRepository.save(admin);
        };
    }
}
