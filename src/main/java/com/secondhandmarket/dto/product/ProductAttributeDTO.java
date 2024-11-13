package com.secondhandmarket.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductAttributeDTO {
    @NotNull(message = "Attribute ID is required")
    String attributeId;
    @NotBlank(message = "Value is required")
    String value;
}
