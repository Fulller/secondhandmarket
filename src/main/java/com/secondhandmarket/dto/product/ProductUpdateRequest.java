package com.secondhandmarket.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductUpdateRequest {
    @NotBlank(message = "Product ID is required")
    String productId;

    @NotBlank(message = "Name is required")
    String name;

    @NotBlank(message = "Description is required")
    String description;

    @NotNull(message = "Price is required")
        Double price;

    @NotNull(message = "Address ID is required")
    String addressId;

    String video;

    @NotBlank(message = "Thumbnail is required")
    String thumbnail;

    @Size(min = 1, max = 6, message = "Images is from 1 to 6")
    List<ImageUpdateDTO> images;

    @NotNull(message = "Category ID is required")
    String categoryId;

    @Size(min = 1, message = "The number of product attributes must be at least 1")
    List<ProductAttributeUpdateDTO> productAttributes;
}
