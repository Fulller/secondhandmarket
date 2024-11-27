package com.secondhandmarket.repository;

import com.secondhandmarket.model.PurchaseRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseRequestRepository extends JpaRepository<PurchaseRequest, String> {
    List<PurchaseRequest> findByBuyerId(String buyerId); // Truy vấn theo ID người mua

    List<PurchaseRequest> findByProduct_SellerId(String sellerId); // Truy vấn theo ID người bán

    List<PurchaseRequest> findByProduct_Id(String productId); // Truy vấn theo ID sản phẩm

}
