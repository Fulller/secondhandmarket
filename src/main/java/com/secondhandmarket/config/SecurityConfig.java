package com.secondhandmarket.config;

import com.secondhandmarket.security.CustomOAuth2UserService;
import com.secondhandmarket.service.thymeleaf.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final String[] POST_PUBLIC_ROUTES = {"/auth/**", "/", "/login", "/oauth2/**"};
    private final String[] GET_PUBLIC_ROUTES = {"/auth/**", "/", "/login", "/oauth2/**"};


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

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Cho phép tất cả các nguồn
        configuration.setAllowedOrigins(Arrays.asList("*"));

        // Cho phép tất cả các phương thức HTTP
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Cho phép tất cả các headers
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

        // Không cần cấu hình allowCredentials nếu cho phép mọi nguồn mà không cần gửi cookie
        configuration.setAllowCredentials(false);
        configuration.setMaxAge(3600L); // Giới hạn thời gian cache

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable()) // Disable CSRF if not needed
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Enable CORS with your configuration
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, POST_PUBLIC_ROUTES).permitAll()
                        .requestMatchers(HttpMethod.GET, GET_PUBLIC_ROUTES).permitAll()
                        .requestMatchers(("/css/**")).permitAll()
                        .requestMatchers(("/js/**")).permitAll()
                        .requestMatchers(("/images/**")).permitAll()
                        .requestMatchers(("/api/**")).permitAll()
                        .requestMatchers(("/dashboard/**")).authenticated()
                        .requestMatchers("/sign-in").anonymous()
                        .anyRequest().authenticated()
                );

        httpSecurity.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

        httpSecurity.formLogin(form -> form
                        .loginPage("/sign-in")
                        .loginProcessingUrl("/sign-in")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/dashboard")
//                .successHandler((request, response, authentication) -> {
//                    response.sendRedirect("/dashboard");
//                })
                        .failureUrl("/sign-in?error=true")
                        .permitAll()
        );

        httpSecurity.logout(logout -> logout
                .logoutUrl("/log-out")
                .logoutSuccessUrl("/sign-in")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
        );

        httpSecurity.oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2UserService)
                        )
                        .defaultSuccessUrl("/auth/login/oauth2/success", true)
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwtConfigurer -> jwtConfigurer
                                .decoder(jwtDecoder)
                                .jwtAuthenticationConverter(jwtAuthenticationConverter)
                        )
                );

        return httpSecurity.build();
    }
}
