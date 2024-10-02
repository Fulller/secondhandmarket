package com.secondhandmarket.service.attribute;

import com.secondhandmarket.dto.AttributeRequest;
import com.secondhandmarket.model.Attribute;
import com.secondhandmarket.repository.AttributeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class AttributeService implements IAttributeService {
    @Autowired
    private final AttributeRepository attributeRepository;

    @Override
    public void saveAttribute(AttributeRequest attributeRequest) {
        Attribute attribute = new Attribute();
        attribute.setName(attributeRequest.getName());
        attributeRepository.save(attribute);
    }
}
