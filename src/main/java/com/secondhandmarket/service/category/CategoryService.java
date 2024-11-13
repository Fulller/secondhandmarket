package com.secondhandmarket.service.category;

import com.secondhandmarket.dto.category.CategoryChildRequest;
import com.secondhandmarket.dto.category.CategoryParentRequest;
import com.secondhandmarket.exception.AppException;
import com.secondhandmarket.model.Attribute;
import com.secondhandmarket.model.Category;
import com.secondhandmarket.model.Option;
import com.secondhandmarket.repository.AttributeRepository;
import com.secondhandmarket.repository.CategoryRepository;
import com.secondhandmarket.repository.OptionRepository;
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
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final AttributeRepository attributeRepository;
    private final OptionRepository optionRepository;

    public void saveCategoryParent(CategoryParentRequest categoryParentRequest) {
        Category category = new Category();
        category.setId(UUID.randomUUID().toString());
        category.setName(categoryParentRequest.getName());
        category.setLevel(categoryParentRequest.getLevel());
        categoryRepository.save(category);
    }

    public void saveCategoryChild(CategoryChildRequest categoryChildRequest) {
        Category category = new Category();
        category.setId(UUID.randomUUID().toString());
        category.setName(categoryChildRequest.getName());
        category.setLevel(categoryChildRequest.getLevel());
        if (categoryChildRequest.getParent() != null) {
            category.setParent(categoryChildRequest.getParent());
        }
        categoryRepository.save(category);
    }

    public void deleteCategory(String id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()) {
            categoryRepository.delete(category.get());
        } else {
            throw new AppException(HttpStatus.NOT_FOUND, "Category not found with id: " + id);
        }
    }

    public Optional<Category> findById(String id) {
        return categoryRepository.findById(id);
    }

    public Page<Category> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    public void updateCategoryParent(String id, CategoryParentRequest categoryParentRequest) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            Category existingCategoryParent = categoryOptional.get();
            existingCategoryParent.setName(categoryParentRequest.getName());
            categoryRepository.save(existingCategoryParent);
        } else {
            throw new AppException(HttpStatus.NOT_FOUND, "Category not found with id: " + id);
        }
    }

    public void updateCategoryChild(String id, CategoryChildRequest categoryChildRequest) {
        Optional<Category> attributeOptional = categoryRepository.findById(id);
        if (attributeOptional.isPresent()) {
            Category existingCategoryChild = attributeOptional.get();
            existingCategoryChild.setName(categoryChildRequest.getName());
            existingCategoryChild.setLevel(categoryChildRequest.getLevel());
            if (categoryChildRequest.getParent() != null) {
                existingCategoryChild.setParent(categoryChildRequest.getParent());
            }
            categoryRepository.save(existingCategoryChild);
        } else {
            throw new AppException(HttpStatus.NOT_FOUND, "Category not found with id: " + id);
        }
    }

    public Page<Category> findAllCategoryParent(Pageable pageable) {
        return categoryRepository.findAllByParentIsNull(pageable);
    }

    public List<Category> findAllCategoryParent() {
        return categoryRepository.findAllByParentIsNull();
    }

    public List<Category> findAllCategoryChild() {
        return categoryRepository.findAllByParentIsNotNull();
    }

    public Page<Category> findAllCategoryChild(Pageable pageable) {
        return categoryRepository.findAllByParentIsNotNull(pageable);
    }

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

    public List<Attribute> getAttributesByCategoryId(String categoryId) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);

        if (categoryOptional.isPresent()) {
            List<Attribute> attributes = attributeRepository.findByCategoryId(categoryId);
            for (Attribute attribute : attributes) {
                List<Option> options = optionRepository.findByAttributeId(attribute.getId());
                attribute.setOptions(options);  // Giả sử bạn có phương thức setOptions trong Attribute
            }
            return attributes;
        } else {
            throw new AppException(HttpStatus.NOT_FOUND, "Category not found with id: " + categoryId);
        }
    }
}
