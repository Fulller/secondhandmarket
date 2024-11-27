package com.secondhandmarket.repository;

import com.secondhandmarket.model.ProductAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductAttributeRepository extends JpaRepository<ProductAttribute, String> {
    void deleteAllByIdNotInAndProductId(List<String> requestAttributeIds, String productId);
}
