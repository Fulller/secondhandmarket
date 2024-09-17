package com.secondhandmarket.service;

import com.secondhandmarket.dto.auth.*;
import com.secondhandmarket.dto.jwt.JWTPayloadDto;
import com.secondhandmarket.model.RefreshToken;
import com.secondhandmarket.model.User;
import com.secondhandmarket.enums.ERole;
import com.secondhandmarket.exception.AppException;
import com.secondhandmarket.mapper.UserMapper;
import com.secondhandmarket.repository.RefreshTokenRepository;
import com.secondhandmarket.repository.UserRepository;
import com.secondhandmarket.util.PasswordUtil;
import com.secondhandmarket.util.jwt.AccessTokenUtil;
import com.secondhandmarket.util.jwt.RefreshTokenUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {
    PasswordUtil passwordUtil;
    UserRepository userRepository;
    UserMapper userMapper;
    AccessTokenUtil accessTokenUtil;
    RefreshTokenUtil refreshTokenUtil;
    RefreshTokenRepository refreshTokenRepository;

    public void register(AuthRegisterRequest request) {
        boolean existedUser = userRepository.existsByEmail(request.getEmail());
        if(existedUser){
            throw new AppException(HttpStatus.BAD_REQUEST, "Email has existed", "auth-e-01");
        }
    }


    public AuthResponse verifyRegister(AuthRegisterRequest request) {
        // Find user if not existed
        boolean existedUser = userRepository.existsByEmail(request.getEmail());
        if(existedUser){
            throw new AppException(HttpStatus.BAD_REQUEST, "Email has existed", "auth-e-01");
        }
        // Hash password
        String hashedPassword = passwordUtil.encodePassword(request.getPassword());
        request.setPassword(hashedPassword);

        // Roles for normal user
        Set<ERole> roles = new HashSet<>();
        roles.add(ERole.USER);
        User user = User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
        user.setRoles(roles);
        userRepository.save(user);

        // Generate a pair of token
        String accessTokenString =  accessTokenUtil.generateToken(userMapper.toJWTPayloadDto(user));
        String refreshTokenString =  refreshTokenUtil.generateToken(userMapper.toJWTPayloadDto(user),user);
        return AuthResponse.builder()
                .accessToken(accessTokenString)
                .refreshToken(refreshTokenString)
                .build();
    }

    public AuthResponse login(AuthLoginRequest request){
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(
                ()-> new AppException(HttpStatus.NOT_FOUND, "Email user not found", "auth-e-02")
        );
        boolean isMatchPassword = passwordUtil.checkPassword(request.getPassword(), user.getPassword());
        if(!isMatchPassword){
            throw new AppException(HttpStatus.BAD_REQUEST, "Wrong password", "auth-e-03");
        }
        String accessTokenString =  accessTokenUtil.generateToken(userMapper.toJWTPayloadDto(user));
        String refreshTokenString =  refreshTokenUtil.generateToken(userMapper.toJWTPayloadDto(user),user);
        return AuthResponse.builder()
                .accessToken(accessTokenString)
                .refreshToken(refreshTokenString)
                .build();
    }

    public AuthResponse refreshToken(AuthRefreshTokenRequest request){
        JWTPayloadDto payload = refreshTokenUtil.verifyToken(request.getRefreshToken());
        String accessTokenString =  accessTokenUtil.generateToken(payload);
        return AuthResponse.builder()
                .accessToken(accessTokenString)
                .build();
    }

    public void logOut(AuthLogOutRequest request){
        JWTPayloadDto payload = refreshTokenUtil.verifyToken(request.getRefreshToken());
        RefreshToken refreshToken =  refreshTokenRepository
                .findByUserId(payload.getId())
                .orElseThrow(
                        () -> new AppException(HttpStatus.NOT_FOUND, "Refresh token not found", "auth-e-04")
                );
        refreshToken.setToken(null);
        refreshTokenRepository.save(refreshToken);
    }

    public void changePassword(String userId, AuthChangePasswordRequest request){
        User user = userRepository
                .findById(userId)
                .orElseThrow(
                        () -> new AppException(HttpStatus.NOT_FOUND, "User not found", "auth-e-05")
                );
        if(user.getIsFromOutside()){
            throw new AppException(HttpStatus.BAD_REQUEST, "User is not internal", "auth-e-06");
        }
        boolean isMatchPassword = passwordUtil.checkPassword(request.getCurrentPassword(), user.getPassword());
        if(!isMatchPassword){
            throw new AppException(HttpStatus.BAD_REQUEST, "Wrong current password", "auth-e-07");
        }
        String hashedNewPassword = passwordUtil.encodePassword(request.getNewPassword());
        user.setPassword(hashedNewPassword);
        userRepository.save(user);
    }
}
