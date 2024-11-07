package com.secondhandmarket.service.blog;

import com.secondhandmarket.enums.ProductStatus;
import com.secondhandmarket.model.Product;
import com.secondhandmarket.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogService {
    private final ProductRepository productRepository;

    public Page<Product> findAllProductPending(Pageable pageable) {
        return productRepository.findAllByStatusIsPending(pageable);
    }

    public Product updateProductAvailable(String id) {
        Product oldProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy id"));
        oldProduct.setStatus(ProductStatus.AVAILABLE);
        return productRepository.save(oldProduct); // Return updated product
    }

    public Product updateProductRemove(String id) {
        Product oldProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy id"));
        oldProduct.setStatus(ProductStatus.REMOVED);
        return productRepository.save(oldProduct); // Return updated product
    }
}
