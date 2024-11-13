package com.secondhandmarket.dto.product;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductAttributeUpdateDTO {
    String id;  // ID của attribute nếu đã tồn tại
    String attributeId;
    String value;
}
