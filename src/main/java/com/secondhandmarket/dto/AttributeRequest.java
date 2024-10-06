package com.secondhandmarket.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AttributeRequest {
    @NotBlank(message = "Bạn chưa nhập tên thuộc tính!")
    private String name;
}
