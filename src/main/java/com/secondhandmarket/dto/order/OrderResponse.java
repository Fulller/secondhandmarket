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
    ProductDTO product;
    UserDTO seller;
    UserDTO buyer;
    PurchaseRequestDTO purchaseRequest;
    OrderStatus status;
    LocalDateTime created_at;
    LocalDateTime updated_at;

    @Data
    public static class PurchaseRequestDTO{
        String id;
        String buyerId;
        String message;
        LocalDateTime requested_at;

        LocalDateTime accepted_at;
    }

    @Data
    public static class ProductDTO {
        String id;
        String name;
        Double price;
        String thumbnail;
    }

    @Data
    public static class UserDTO {
        String id;
        String name;
        String phone;
        String email;
        String avatar;
        Double rating;
    }
}
