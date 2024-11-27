package com.secondhandmarket.dto.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.secondhandmarket.dto.user.UserSellerInProductDTO;
import com.secondhandmarket.enums.ProductStatus;
import com.secondhandmarket.model.Address;
import com.secondhandmarket.model.Category;
import com.secondhandmarket.model.Image;
import com.secondhandmarket.model.ProductAttribute;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductPostingResponse {
    String id;
    String name;
    String slug;
    String description;
    Double price;
    ProductStatus status;
    Address address;
    LocalDateTime posted_at;
    String video;
    String thumbnail;
    UserSellerInProductDTO seller;
    Category category;
    List<Image> images;
    List<ProductAttribute> productAttributes;
}
