package com.secondhandmarket.repository;

import com.secondhandmarket.dto.request.UserUpdateRequest;
import com.secondhandmarket.dto.response.UserResponse;
import org.mapstruct.MappingTarget;
import org.springframework.data.jpa.repository.JpaRepository;
import com.secondhandmarket.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmailAndIsFromOutsideFalse(String email);
    Optional<User> findByEmailAndIsFromOutsideFalse(String email);
    Optional<User> findByIsFromOutsideTrueAndProviderNameAndProviderId(String providerName, String providerId);
}
