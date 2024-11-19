package com.secondhandmarket.service.blog;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.secondhandmarket.enums.ProductStatus;
import com.secondhandmarket.model.Product;
import com.secondhandmarket.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import org.springframework.core.io.ResourceLoader;


@Service
@RequiredArgsConstructor
public class BlogService {
    private final ProductRepository productRepository;

    private final ResourceLoader resourceLoader;

    public Page<Product> findAllProductPending(Pageable pageable) {
        return productRepository.findAllByStatusIsPending(pageable);
    }

    public Page<Product> findAllProductAvailable(Pageable pageable) {
        return productRepository.findAllByStatusIsAvailable(pageable);
    }

    public Page<Product> findAllProductRejected(Pageable pageable) {
        return productRepository.findAllByStatusIsRejected(pageable);
    }

    public Page<Product> findAllProductHidden(Pageable pageable) {
        return productRepository.findAllByStatusIsHidden(pageable);
    }

    public Product updateProductAvailable(String id) {
        Product oldProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy id"));
        oldProduct.setStatus(ProductStatus.AVAILABLE);
        return productRepository.save(oldProduct);
    }

    public Product updateProductRemove(String id) {
        Product oldProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy id"));
        oldProduct.setStatus(ProductStatus.REJECTED);
        return productRepository.save(oldProduct);
    }

    public Product updateProductHidden(String id) {
        Product oldProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy id"));
        oldProduct.setStatus(ProductStatus.HIDDEN);
        return productRepository.save(oldProduct);
    }

    public Product getProduct(String id) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            return productOptional.get();
        } else {
            throw new RuntimeException("Không tìm thấy sản phẩm với id: " + id);
        }
    }

    public Map<String, Object> getAddressData(Product product) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:/static/json/address.json");
        if (!resource.exists()) {
            throw new IOException("Không tìm thấy file JSON tại classpath:/static/json/address.json");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> addressData = objectMapper.readValue(resource.getInputStream(), new TypeReference<Map<String, Object>>() {});
        String provinceCode = product.getAddress().getProvince();
        Map<String, Object> province = (Map<String, Object>) addressData.get(provinceCode);
        String districtCode = product.getAddress().getDistrict();
        Map<String, Object> district = (Map<String, Object>) ((Map<String, Object>) province.get("district")).get(districtCode);
        String wardCode = product.getAddress().getWard();
        Map<String, Object> ward = (Map<String, Object>) ((Map<String, Object>) district.get("ward")).get(wardCode);
        return Map.of(
                "provinceName", province.get("name"),
                "districtName", district.get("name"),
                "wardName", ward.get("name")
        );
    }

}
