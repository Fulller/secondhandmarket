package com.secondhandmarket.service.blog;

import com.secondhandmarket.enums.ProductStatus;
import com.secondhandmarket.model.Product;
import com.secondhandmarket.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BlogService {
    private final ProductRepository productRepository;

    public Page<Product> findAllProductPending(Pageable pageable) {
        return productRepository.findAllByStatusIsPending(pageable);
    }

    public Page<Product> findAllProductAvailable(Pageable pageable) {
        return productRepository.findAllByStatusIsAvailable(pageable);
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
        oldProduct.setStatus(ProductStatus.REJECTED);
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
}
