package com.secondhandmarket.dto.attribute;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AttributeRequest {
    private String categoryId;

    @NotBlank(message = "Bạn chưa nhập tên thuộc tính!")
    private String name;

    private String options;

    private Boolean isRequired;

    private Boolean isEnter;
}
