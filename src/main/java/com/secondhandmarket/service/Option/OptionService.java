package com.secondhandmarket.service.Option;

import com.secondhandmarket.dto.option.OptionRequest;
import com.secondhandmarket.exception.AppException;
import com.secondhandmarket.model.Attribute;
import com.secondhandmarket.model.Option;
import com.secondhandmarket.repository.AttributeRepository;
import com.secondhandmarket.repository.OptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class OptionService {
    private final OptionRepository optionRepository;
    private final AttributeRepository attributeRepository;

    public void saveOption(OptionRequest request, String attributeId) {
        Option option = new Option();
        Attribute attribute = attributeRepository.findById(attributeId).orElse(null);
        option.setAttribute(attribute);
        option.setId(UUID.randomUUID().toString());
        option.setName(request.getName());
        optionRepository.save(option);
    }

    public Optional<Option> findById(String id) {
        return optionRepository.findById(id);
    }

    public void updateOption(String id, OptionRequest request) {
        Optional<Option> option = optionRepository.findById(id);
        if (option.isPresent()) {
            Option currentOption = option.get();
            currentOption.setName(request.getName());
            optionRepository.save(currentOption);
        } else {
            throw new AppException(HttpStatus.NOT_FOUND, "Option not found with id: " + id);
        }
    }

    public void deleteOption(String id) {
        Optional<Option> option = optionRepository.findById(id);
        if (option.isPresent()) {
            optionRepository.delete(option.get());
        } else {
            throw new AppException(HttpStatus.NOT_FOUND, "Option not found with id: " + id);
        }
    }

    public Page<Option> findAll(Pageable pageable) {
        return optionRepository.findAll(pageable);
    }



}
