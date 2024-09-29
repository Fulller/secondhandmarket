package com.secondhandmarket.config;

import com.secondhandmarket.security.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final String[] POST_PUBLIC_ROUTES = {"/auth/**","/", "/login", "/signin", "/oauth2/**"};
    private final String[] GET_PUBLIC_ROUTES = {"/auth/**","/", "/login", "/oauth2/**"};
//private final String[] GET_PUBLIC_ROUTES = {"/", "/login", "/oauth2/**"};

    @Autowired
    public JwtDecoder jwtDecoder;

    @Autowired
    public JwtAuthenticationConverter jwtAuthenticationConverter;

    @Autowired
    private CustomOAuth2UserService oAuth2UserService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(request-> request
                        .requestMatchers(HttpMethod.POST, POST_PUBLIC_ROUTES).permitAll()
                        .requestMatchers(HttpMethod.GET, GET_PUBLIC_ROUTES).permitAll()
                        .requestMatchers(("/css/output.css")).permitAll()
                        .anyRequest().authenticated()
                );

        httpSecurity.formLogin(form -> form
                .loginPage("/sign-in")
                .permitAll()
        );
        httpSecurity.logout(logout -> logout
                .logoutSuccessUrl("/sign-in")
        );

        httpSecurity.oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> userInfo
                        .userService(oAuth2UserService)
                )
                .defaultSuccessUrl("/auth/login/oauth2/success", true)
        );

        httpSecurity.oauth2ResourceServer(oauth2->
                oauth2.jwt(jwtConfigurer -> jwtConfigurer
                        .decoder(jwtDecoder)
                        .jwtAuthenticationConverter(jwtAuthenticationConverter)
                )
        );
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }

}

