package com.secondhandmarket.util.jwt;

import com.secondhandmarket.dto.jwt.JWTPayloadDto;
import com.secondhandmarket.exception.AppException;
import com.secondhandmarket.model.RefreshToken;
import com.secondhandmarket.model.User;
import com.secondhandmarket.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class RefreshTokenUtil extends BaseJWTUtil {
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${app.jwt.refresh.secret}")
    private String refreshSecret;

    @Value("${app.jwt.refresh.expiration}")
    private long refreshExpiration;

    @Override
    protected String getSecret() {
        return refreshSecret;
    }

    @Override
    protected long getExpiration() {
        return refreshExpiration;
    }

    public String generateToken(JWTPayloadDto payload, User user) {
        RefreshToken refreshToken = refreshTokenRepository
                .findByUser(user)
                .orElseGet(() -> RefreshToken.builder().user(user).build());
        String token = super.generateToken(payload);
        refreshToken.setToken(token);
        refreshTokenRepository.save(refreshToken);
        return token;
    }


    @Override
    public JWTPayloadDto verifyToken(String token) {
        JWTPayloadDto payload = super.verifyToken(token);
        RefreshToken refreshToken =  refreshTokenRepository
                .findByUserId(payload.getId())
                .orElseThrow(
                        () -> new AppException(HttpStatus.NOT_FOUND, "Refresh token not found", "auth-e-01")
                );
        if(refreshToken.getToken() == null || !refreshToken.getToken().equals(token)){
            throw new AppException(HttpStatus.NOT_FOUND, "Refresh token not found", "auth-e-01");
        }
        return payload;
    }
}
