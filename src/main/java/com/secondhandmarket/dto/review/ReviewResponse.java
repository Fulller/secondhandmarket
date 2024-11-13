package com.secondhandmarket.dto.review;

import com.secondhandmarket.enums.ReviewType;
import com.secondhandmarket.model.Product;
import com.secondhandmarket.model.User;
import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewResponse {
    String id;
    Integer rating;
    String comment;
    String image;
    String feedbackFromSeller;
    ProductDTO product;
    UserDTO seller;
    UserDTO reviewer;
    ReviewType reviewType;
    LocalDateTime created_at;

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
