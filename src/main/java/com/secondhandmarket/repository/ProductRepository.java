package com.secondhandmarket.repository;

import com.secondhandmarket.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, String> {

    @Query("SELECT p FROM Product p WHERE p.status = 'PENDING'")
    Page<Product> findAllByStatusIsPending(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.status = 'AVAILABLE'")
    Page<Product> findAllByStatusIsAvailable(Pageable pageable);
}
