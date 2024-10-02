package com.secondhandmarket.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AttributeRequest {
    @NotNull(message = "Bạn chưa nhập tên thuộc tính!")
    private String name;
}
