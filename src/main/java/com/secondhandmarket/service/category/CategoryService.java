package com.secondhandmarket.service.category;

import com.secondhandmarket.dto.category.CategoryChildRequest;
import com.secondhandmarket.dto.category.CategoryParentRequest;
import com.secondhandmarket.exception.AppException;
import com.secondhandmarket.model.Attribute;
import com.secondhandmarket.model.Category;
import com.secondhandmarket.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

    @Autowired
    private final CategoryRepository categoryRepository;

    @Override
    public void saveCategoryParent(CategoryParentRequest categoryParentRequest) {
        Category category = new Category();
        category.setId(UUID.randomUUID().toString());
        category.setName(categoryParentRequest.getName());
        category.setLevel(categoryParentRequest.getLevel());
        categoryRepository.save(category);
    }

    @Override
    public void saveCategoryChild(CategoryChildRequest categoryChildRequest) {
        Category category = new Category();
        category.setId(UUID.randomUUID().toString());
        category.setName(categoryChildRequest.getName());
        category.setLevel(categoryChildRequest.getLevel());
        if (categoryChildRequest.getParent() != null) {
            category.setParent(categoryChildRequest.getParent());
        }
        for (Attribute attribute : categoryChildRequest.getAttributes()) {
            category.getAttributes().add(attribute);
            attribute.getCategories().add(category);
        }
        categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(String id) {

    }

    @Override
    public Optional<Category> findById(String id) {
        return categoryRepository.findById(id);
    }

    public Page<Category> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @Override
    public void updateCategoryParent(String id, CategoryParentRequest categoryParentRequest) {
        Optional<Category> attributeOptional = categoryRepository.findById(id);
        if (attributeOptional.isPresent()) {
            Category existingCategoryParent = attributeOptional.get();
            existingCategoryParent.setName(categoryParentRequest.getName());
            categoryRepository.save(existingCategoryParent);
        } else {
            throw new AppException(HttpStatus.NOT_FOUND, "Category not found with id: " + id);
        }
    }

    @Override
    public void updateCategoryChild(String id, CategoryChildRequest categoryChildRequest) {

    }

    @Override
    public List<Category> findAllCategoryParent() {
        return categoryRepository.findAllByParentNull();
    }

    @Override
    public List<Category> getCategoryTree() {
        List<Category> categories = categoryRepository.findAll();
        Map<String, Category> categoryMap = new HashMap<>();

        // Đầu tiên, thêm tất cả các danh mục vào map
        for (Category category : categories) {
            categoryMap.put(category.getId(), category);
        }

        List<Category> categoryTree = new ArrayList<>();

        // Xây dựng cây danh mục
        for (Category category : categories) {
            if (category.getParent() == null) {
                // Nếu danh mục không có cha, thêm vào cây
                categoryTree.add(category);
            } else {
                // Nếu có cha, tìm cha và thêm danh mục vào danh sách con của cha
                Category parent = categoryMap.get(category.getParent().getId());
                if (parent != null) {
                    // Kiểm tra xem danh mục đã có trong danh sách con của cha chưa
                    if (!parent.getCategoryChildren().contains(category)) {
                        parent.getCategoryChildren().add(category);
                    }
                }
            }
        }
        return categoryTree;
    }

    @Override
    public Set<Attribute> getAttributesByCategoryId(String categoryId) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if (categoryOptional.isPresent()) {
            return categoryOptional.get().getAttributes();
        } else {
            throw new EntityNotFoundException("Danh mục không tồn tại");
        }
    }
}
