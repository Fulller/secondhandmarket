package com.secondhandmarket.service.attribute;

import com.secondhandmarket.dto.attribute.AttributeRequest;
import com.secondhandmarket.exception.AppException;
import com.secondhandmarket.model.Attribute;
import com.secondhandmarket.model.Option;
import com.secondhandmarket.repository.AttributeRepository;
import com.secondhandmarket.repository.CategoryRepository;
import com.secondhandmarket.repository.OptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class AttributeService {
    private final AttributeRepository attributeRepository;
    private final CategoryRepository categoryRepository;

    public void saveAttribute(AttributeRequest attributeRequest) {
        // Tạo đối tượng Attribute mới
        Attribute attribute = new Attribute();
        attribute.setId(UUID.randomUUID().toString());
        attribute.setName(attributeRequest.getName());
        attribute.setCategory(categoryRepository.findById(attributeRequest.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found")));

        // Kiểm tra nếu chuỗi options không rỗng
        List<Option> options = new ArrayList<>();
        if (attributeRequest.getOptions() != null && !attributeRequest.getOptions().isEmpty()) {
            // Phân tách chuỗi các Option và xử lý chúng
            String[] optionNames = attributeRequest.getOptions().split("\\s+");

            for (String optionName : optionNames) {
                // Loại bỏ dấu nháy kép ở đầu và cuối (nếu có)
                optionName = optionName.replaceAll("^\"|\"$", "").trim();

                // Nếu optionName không phải là chuỗi rỗng
                if (!optionName.isEmpty()) {
                    Option option = new Option();
                    option.setId(UUID.randomUUID().toString());
                    option.setName(optionName);
                    option.setAttribute(attribute);

                    options.add(option);
                }
            }
        }

        // Cập nhật giá trị của isRequired và isEnter từ attributeRequest
        attribute.setIsRequired(attributeRequest.getIsRequired() != null && attributeRequest.getIsRequired());
        attribute.setIsEnter(attributeRequest.getIsEnter() != null && attributeRequest.getIsEnter());

        // Gán danh sách Option cho Attribute nếu có options
        attribute.setOptions(options);

        // Lưu Attribute (bao gồm các Option)
        attributeRepository.save(attribute);
    }

    public Page<Attribute> findAll(Pageable pageable) {
        return attributeRepository.findAll(pageable); // Gọi phương thức tìm tất cả với phân trang
    }

    public void deleteAttribute(String id) {
        Optional<Attribute> attribute = attributeRepository.findById(id);
        if (attribute.isPresent()) {
            attributeRepository.delete(attribute.get());
        } else {
            throw new AppException(HttpStatus.NOT_FOUND, "Attribute not found with id: " + id);
        }
    }

    public Optional<Attribute> findById(String id) {
        return attributeRepository.findById(id);
    }

    public void updateAttribute(String id, AttributeRequest attribute) {
        Optional<Attribute> attributeOptional = attributeRepository.findById(id);
        if (attributeOptional.isPresent()) {
            Attribute existingAttribute = attributeOptional.get();
            existingAttribute.setName(attribute.getName());
            attributeRepository.save(existingAttribute);
        } else {
            throw new AppException(HttpStatus.NOT_FOUND, "Attribute not found with id: " + id);
        }
    }

    public List<Attribute> findAll() {
        return attributeRepository.findAll();
    }
}
