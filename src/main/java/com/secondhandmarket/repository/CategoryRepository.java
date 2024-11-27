package com.secondhandmarket.repository;

import com.secondhandmarket.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, String> {
    Boolean existsByName(String name);
    List<Category> findAllByParentIsNull();
    List<Category> findAllByParentIsNotNull();
    Page<Category> findAllByParentIsNull(Pageable pageable);
    Page<Category> findAllByParentIsNotNull(Pageable pageable);
    Boolean existsByCategoryChildrenIsNotEmptyAndId(String id);
    Boolean existsByAttributesIsNotEmptyAndId(String id);
}
