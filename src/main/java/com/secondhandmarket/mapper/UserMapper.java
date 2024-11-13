package com.secondhandmarket.mapper;

import com.secondhandmarket.dto.auth.AuthUserInfoResponse;
import com.secondhandmarket.dto.jwt.JWTPayloadDto;
import com.secondhandmarket.dto.user.UserResponse;
import com.secondhandmarket.dto.user.UserSellerInProductDTO;
import com.secondhandmarket.dto.user.UserUpdateRequest;
import com.secondhandmarket.enums.ERole;
import com.secondhandmarket.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "roles", target = "scope", qualifiedByName = "rolesToScope")
    JWTPayloadDto toJWTPayloadDto(User user);

    @org.mapstruct.Named("rolesToScope")
    static String rolesToScope(Set<ERole> roles) {
        StringBuilder scopeBuilder = new StringBuilder();
        for (ERole role : roles) {
            scopeBuilder.append(String.format("ROLE_%s ", role.name()));
        }
        return scopeBuilder.toString().trim();
    }

    void toUpdateUser(@MappingTarget User user, UserUpdateRequest request);

    UserResponse toResponseUser(User user);

    AuthUserInfoResponse toUserInfo(User user);

    UserSellerInProductDTO toUserSellerInProductDto(User user);
}