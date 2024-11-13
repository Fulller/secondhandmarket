package com.secondhandmarket.dto.product;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImageUpdateDTO {
    String id;  // ID của image nếu đã tồn tại
    String url;
}
