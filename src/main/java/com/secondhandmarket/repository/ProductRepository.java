package com.secondhandmarket.repository;

import com.secondhandmarket.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {

    @Query("SELECT p FROM Product p WHERE p.status = 'PENDING'")
    Page<Product> findAllByStatusIsPending(Pageable pageable);
}
