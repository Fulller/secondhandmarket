package com.secondhandmarket.controller;

import com.secondhandmarket.dto.api.ApiResponse;
import com.secondhandmarket.model.Attribute;
import com.secondhandmarket.model.Category;
import com.secondhandmarket.service.attribute.AttributeService;
import com.secondhandmarket.service.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")

public class CategoryApiController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AttributeService attributeService;

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<Category>>> getCategoryTree() {
        List<Category> categories = categoryService.getCategoryTree();
        ApiResponse<List<Category>> apiResponse = ApiResponse.<List<Category>>builder()
                .data(categories)
                .code("category-s-01")
                .message("Lấy danh mục thành công")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }


    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<ApiResponse<List<Attribute>>> getAttributesByCategoryId(@PathVariable String categoryId) {
        List<Attribute> attributes = categoryService.getAttributesByCategoryId(categoryId);
        ApiResponse<List<Attribute>> apiResponse = ApiResponse.<List<Attribute>>builder()
                .data(attributes)
                .code("attribute-s-01")
                .message("Lấy thuộc tính thành công")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/list-category-child/{categoryId}")
    public ResponseEntity<List<Category>> getCategoryChild(@PathVariable String categoryId) {
        List<Category> categories = categoryService.getCategoryChildByCategoryId(categoryId);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/attribute/{categoryId}")
    public ResponseEntity<List<Attribute>> getAttributesByCategoryIdThymeLeaf(@PathVariable String categoryId) {
        List<Attribute> attributes = categoryService.getAttributesByCategoryId(categoryId);
        return ResponseEntity.ok(attributes);
    }

}
