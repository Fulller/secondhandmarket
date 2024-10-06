package com.secondhandmarket.dto.category;

import com.secondhandmarket.model.Attribute;
import com.secondhandmarket.model.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class CategoryChildRequest {
    @NotBlank(message = "Bạn chưa nhập tên danh mục con!")
    private String name;

    private Integer level = 2;

    private Category parent;

    @Size(min = 1, message = "Bạn cần chọn ít nhất 1 thuộc tính!")
    Set<Attribute> attributes = new HashSet<>();
}
