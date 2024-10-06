package com.secondhandmarket.service.category;

import com.secondhandmarket.dto.category.CategoryChildRequest;
import com.secondhandmarket.dto.category.CategoryParentRequest;
import com.secondhandmarket.model.Attribute;
import com.secondhandmarket.model.Category;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ICategoryService {
    void saveCategoryParent(CategoryParentRequest categoryParentRequest);
    void saveCategoryChild(CategoryChildRequest categoryChildRequest);
    void deleteCategory(String id);
    Optional<Category> findById(String id);
    void updateCategoryParent(String id, CategoryParentRequest categoryParentRequest);
    void updateCategoryChild(String id, CategoryChildRequest categoryChildRequest);
    List<Category> findAllCategoryParent();

    List<Category> getCategoryTree();
    Set<Attribute> getAttributesByCategoryId(String categoryId);
}
