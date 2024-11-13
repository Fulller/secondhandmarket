package com.secondhandmarket.dto.product;

import com.secondhandmarket.dto.user.UserSellerInProductDTO;
import com.secondhandmarket.model.Address;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductTagResponse {
    String id;
    String name;
    String slug;
    Double price;
    Address address;
    LocalDateTime postedAt;
    String thumbnail;
    UserSellerInProductDTO seller;
}