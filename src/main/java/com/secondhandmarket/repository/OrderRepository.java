package com.secondhandmarket.repository;

import com.secondhandmarket.enums.OrderStatus;
import com.secondhandmarket.model.Order;
import com.secondhandmarket.model.Product;
import com.secondhandmarket.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findBySeller(User seller);
    List<Order> findByBuyerAndStatus(User buyer, OrderStatus status);
    List<Order> findBySellerAndStatus(User seller, OrderStatus status);
    List<Order> findByBuyer(User buyer);
    List<Order> findByProduct(Product product);
    boolean existsBySellerAndBuyer(User seller, User buyer);
}
