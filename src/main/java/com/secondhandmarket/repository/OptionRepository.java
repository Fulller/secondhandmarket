package com.secondhandmarket.repository;

import com.secondhandmarket.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<Option, String> {
    boolean existsByName(String name);
}
