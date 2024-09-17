package com.secondhandmarket.service;

import com.secondhandmarket.dto.auth.AuthResponse;
import com.secondhandmarket.dto.auth.AuthLoginRequest;
import com.secondhandmarket.dto.auth.AuthRegisterRequest;
import com.secondhandmarket.model.User;
import com.secondhandmarket.enums.ERole;
import com.secondhandmarket.exception.AppException;
import com.secondhandmarket.mapper.UserMapper;
import com.secondhandmarket.repository.UserRepository;
import com.secondhandmarket.util.JWTUtil;
import com.secondhandmarket.util.PasswordUtil;
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
    JWTUtil jwtUtil;

    public void register(AuthRegisterRequest request) {
        boolean existedUser = userRepository.existsByEmail(request.getEmail());
        if(existedUser){
            throw new AppException(HttpStatus.BAD_REQUEST, "Email has existed.", "auth-e-01");
        }
    }


    public AuthResponse verifyRegister(AuthRegisterRequest request) {
        boolean existedUser = userRepository.existsByEmail(request.getEmail());
        if(existedUser){
            throw new AppException(HttpStatus.BAD_REQUEST, "Email has existed.", "auth-e-01");
        }
        String hashedPassword = passwordUtil.encodePassword(request.getPassword());
        request.setPassword(hashedPassword);
        Set<ERole> roles = new HashSet<>();
        roles.add(ERole.USER);
        User user = User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
        user.setRoles(roles);
        userRepository.save(user);
        String token =  jwtUtil.generateToken(userMapper.toJWTPayloadDto(user));
        return AuthResponse.builder().token(token).build();
    }

    public AuthResponse login(AuthLoginRequest request){
        Optional<User> user = userRepository.findByEmail(request.getEmail());
        if(user.isEmpty()){
            throw new AppException(HttpStatus.NOT_FOUND, "Email user not found.", "auth-e-02");
        }
        boolean isMatchPassword = passwordUtil.checkPassword(request.getPassword(), user.get().getPassword());
        if(!isMatchPassword){
            throw new AppException(HttpStatus.BAD_REQUEST, "Wrong password.", "auth-e-03");
        }
        String token = jwtUtil.generateToken(userMapper.toJWTPayloadDto(user.get()));
        return AuthResponse.builder().token(token).build();
    }
}
