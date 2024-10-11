package com.secondhandmarket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.secondhandmarket.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    boolean existsByEmailAndIsFromOutsideFalse(String email);
    Optional<User> findByEmailAndIsFromOutsideFalse(String email);
    Optional<User> findByIsFromOutsideTrueAndProviderNameAndProviderId(String providerName, String providerId);
}
