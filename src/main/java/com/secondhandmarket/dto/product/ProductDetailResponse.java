package com.secondhandmarket.dto.product;

import com.secondhandmarket.dto.user.UserSellerInProductDTO;
import com.secondhandmarket.enums.ProductStatus;
import com.secondhandmarket.model.Address;
import com.secondhandmarket.model.Category;
import com.secondhandmarket.model.Image;
import com.secondhandmarket.model.ProductAttribute;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDetailResponse {
    String id;
    String name;
    String slug;
    String description;
    Double price;
    ProductStatus status;
    Address address;
    LocalDateTime postedAt;
    LocalDateTime expiredAt;
    String video;
    String thumbnail;
    UserSellerInProductDTO seller;
    Category category;
    List<Image> images;
    List<ProductAttribute> productAttributes;
}