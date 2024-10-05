package com.secondhandmarket.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryParentRequest {
    @NotBlank(message = "Bạn chưa nhập tên danh mục cha!")
    private String name;

    private Integer level = 1;
}
