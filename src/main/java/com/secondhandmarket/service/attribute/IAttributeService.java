package com.secondhandmarket.service.attribute;
import com.secondhandmarket.dto.AttributeRequest;
import com.secondhandmarket.model.Attribute;

import java.util.List;
import java.util.Optional;

public interface IAttributeService {
    void saveAttribute(AttributeRequest attribute);
    void deleteAttribute(String id);
    Optional<Attribute> findById(String id);
    void updateAttribute(String id, AttributeRequest attribute);
    List<Attribute> findAll();
}
