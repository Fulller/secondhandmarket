package com.secondhandmarket.dto.order;

import com.secondhandmarket.enums.OrderStatus;
import com.secondhandmarket.model.Product;
import com.secondhandmarket.model.PurchaseRequest;
import com.secondhandmarket.model.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    String id;
    Product product;
    User seller;
    User buyer;
    PurchaseRequest purchaseRequest;
    OrderStatus status;
    LocalDateTime created_at;
    LocalDateTime updated_at;
}
