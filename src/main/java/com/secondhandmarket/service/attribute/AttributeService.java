package com.secondhandmarket.service.attribute;

import com.secondhandmarket.dto.attribute.AttributeRequest;
import com.secondhandmarket.exception.AppException;
import com.secondhandmarket.model.Attribute;
import com.secondhandmarket.repository.AttributeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class AttributeService {
    private final AttributeRepository attributeRepository;

    public void saveAttribute(AttributeRequest attributeRequest) {
        Attribute attribute = new Attribute();
        attribute.setId(UUID.randomUUID().toString());
        attribute.setName(attributeRequest.getName());
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
