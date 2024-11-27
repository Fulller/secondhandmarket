package com.secondhandmarket.repository;

import com.secondhandmarket.model.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttributeRepository extends JpaRepository<Attribute, String> {
    Boolean existsByName(String name);
    List<Attribute> findByCategoryId(String categoryId);
}
