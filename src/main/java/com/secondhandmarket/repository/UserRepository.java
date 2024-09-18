package com.secondhandmarket.repository;

import com.secondhandmarket.enums.EProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import com.secondhandmarket.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmailAndIsFromOutsideFalse(String email);
    Optional<User> findByEmailAndIsFromOutsideFalse(String email);
    Optional<User> findByIsFromOutsideTrueAndProviderNameAndProviderId(EProvider providerName, String providerId);
}
