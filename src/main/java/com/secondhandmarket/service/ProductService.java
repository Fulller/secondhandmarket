package com.secondhandmarket.service;

import com.secondhandmarket.dto.product.*;
import com.secondhandmarket.enums.ProductStatus;
import com.secondhandmarket.exception.AppException;
import com.secondhandmarket.mapper.ProductMapper;
import com.secondhandmarket.model.*;
import com.secondhandmarket.repository.*;
import com.secondhandmarket.security.SecurityUtil;
import com.secondhandmarket.util.CommonUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductService {
    ProductRepository productRepository;
    ImageRepository imageRepository;
    ProductAttributeRepository productAttributeRepository;
    AddressRepository addressRepository;
    SecurityUtil securityUtil;
    ProductMapper productMapper;
    CategoryRepository categoryRepository;
    AttributeRepository attributeRepository;

    @Transactional
    public ProductPostingResponse post(ProductPostingRequest productPosting) {
        User seller = securityUtil.getCurrentUser();
        Address address = addressRepository
                .findById(productPosting.getAddressId())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Address not found", "address-e-01"));
        if (productRepository.existsByAddress(address)) {
            throw new AppException(HttpStatus.CONFLICT, "Address has been used", "product-e-02");
        }
        Product product = productMapper.toProduct(productPosting);
        product.setSeller(seller);
        product.setAddress(address);
        product.setSlug(CommonUtil.toProductSlug(productPosting.getName()));
        productRepository.save(product);

        // Handle product images
        List<Image> images = productPosting.getImages().stream()
                .map(imageUrl -> {
                    Image image = new Image();
                    image.setUrl(imageUrl);
                    image.setProduct(product);
                    return image;
                })
                .toList();
        images.forEach(imageRepository::save);
        product.getImages().addAll(images);

        // Handle product attributes
        Category category = categoryRepository.findById(productPosting.getCategoryId())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Category not found", "category-e-01"));
        product.setCategory(category);

        // Handle product attributes
        List<ProductAttribute> productAttributes = productPosting.getProductAttributes().stream()
                .map(productAttributeDTO -> {
                    Attribute attribute = attributeRepository.findById(productAttributeDTO.getAttributeId())
                            .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Attribute not found", "attribute-e-01"));
                    return ProductAttribute.builder()
                            .attribute(attribute)
                            .product(product)
                            .value(productAttributeDTO.getValue())
                            .build();
                })
                .toList();
        productAttributeRepository.saveAll(productAttributes);
        product.getProductAttributes().addAll(productAttributes);
        productRepository.save(product);
        return productMapper.toProductPostingResponse(product);
    }

    @Transactional
    public ProductGetBySellerResponse updateProduct(ProductUpdateRequest productUpdateRequest) {
        String sellerId = securityUtil.getCurrentUserId();
        Product product = productRepository.findByIdAndSellerId(productUpdateRequest.getProductId(), sellerId)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Product not found", "product-e-02"));
        Address address = addressRepository
                .findById(productUpdateRequest.getAddressId())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Address not found", "address-e-01"));
        product.setName(productUpdateRequest.getName());
        product.setDescription(productUpdateRequest.getDescription());
        product.setPrice(productUpdateRequest.getPrice());
        product.setAddress(address);
        product.setSlug(CommonUtil.toProductSlug(productUpdateRequest.getName()));
        product.setThumbnail(productUpdateRequest.getThumbnail());
        product.setVideo(productUpdateRequest.getVideo());

        // Xử lý cập nhật images
        List<ImageUpdateDTO> updatedImages = productUpdateRequest.getImages();
        List<String> requestImageIds = updatedImages.stream()
                .map(ImageUpdateDTO::getId)
                .filter(id -> id != null)
                .toList();

        // Xóa những ảnh không còn trong request
        List<Image> currentImages = product.getImages();
        currentImages.removeIf(image -> !requestImageIds.contains(image.getId()));
        imageRepository.deleteAllByIdNotInAndProductId(requestImageIds, product.getId());

        // Cập nhật hoặc thêm mới các ảnh trong request
        updatedImages.forEach(imageDTO -> {
            if (imageDTO.getId() != null) {
                // Nếu ảnh đã tồn tại thì cập nhật URL
                currentImages.stream()
                        .filter(image -> image.getId().equals(imageDTO.getId()))
                        .findFirst()
                        .ifPresent(existingImage -> {
                            existingImage.setUrl(imageDTO.getUrl());
                            imageRepository.save(existingImage);
                        });
            } else {
                // Nếu ảnh mới (không có ID), thì tạo mới
                Image newImage = new Image();
                newImage.setUrl(imageDTO.getUrl());
                newImage.setProduct(product);
                imageRepository.save(newImage);
                product.getImages().add(newImage);
            }
        });

        // Xử lý cập nhật thuộc tính (attributes)
        List<ProductAttributeUpdateDTO> updatedAttributes = productUpdateRequest.getProductAttributes();
        List<String> requestAttributeIds = updatedAttributes.stream()
                .map(ProductAttributeUpdateDTO::getId)
                .filter(id -> id != null)
                .toList();

        // Xóa những thuộc tính không còn trong request
        List<ProductAttribute> currentAttributes = product.getProductAttributes();
        currentAttributes.removeIf(attribute -> !requestAttributeIds.contains(attribute.getId()));
        productAttributeRepository.deleteAllByIdNotInAndProductId(requestAttributeIds, product.getId());

        // Cập nhật hoặc thêm mới các thuộc tính trong request
        updatedAttributes.forEach(attrDTO -> {
            if (attrDTO.getId() != null) {
                // Nếu thuộc tính đã tồn tại thì cập nhật giá trị
                currentAttributes.stream()
                        .filter(attribute -> attribute.getId().equals(attrDTO.getId()))
                        .findFirst()
                        .ifPresent(existingAttribute -> {
                            existingAttribute.setValue(attrDTO.getValue());
                            productAttributeRepository.save(existingAttribute);
                        });
            } else {
                // Nếu thuộc tính mới (không có ID), thì tạo mới
                Attribute attribute = attributeRepository.findById(attrDTO.getAttributeId())
                        .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Attribute not found", "attribute-e-01"));
                ProductAttribute newAttribute = new ProductAttribute();
                newAttribute.setAttribute(attribute);
                newAttribute.setValue(attrDTO.getValue());
                newAttribute.setProduct(product);
                productAttributeRepository.save(newAttribute);
                product.getProductAttributes().add(newAttribute);
            }
        });

        // Save product
        if (ProductStatus.AVAILABLE.equals(product.getStatus())) {
            product.setStatus(ProductStatus.PENDING);
        }
        productRepository.save(product);
        return productMapper.toProductGetBySellerResponse(product);
    }

    public List<ProductGetBySellerResponse> getAllBySeller() {
        String sellerId = securityUtil.getCurrentUserId();
        List<Product> products = productRepository
                .findBySellerId(sellerId);
        return productMapper.toListProductGetBySellerResponse(products);
    }

    public ProductGetBySellerResponse getOneBySeller(String productId) {
        String sellerId = securityUtil.getCurrentUserId();
        Product product = productRepository
                .findByIdAndSellerId(productId, sellerId)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Product not found", "product-e-02"));
        return productMapper.toProductGetBySellerResponse(product);
    }

    public Page<ProductTagResponse> getHomeProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("postedAt").descending());
        Page<Product> productPage = productRepository.findAllByStatus(ProductStatus.AVAILABLE, pageable);
        return productMapper.toPageProductTagResponse(productPage);
    }

    public Page<ProductTagResponse> searchProducts(String query, String province, String categoryId, Double minPrice, Double maxPrice, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        log.info("Executing search query with parameters: query={}, province={}, categoryId={}, minPrice={}, maxPrice={}, pageable={}",
                query, province, categoryId, minPrice, maxPrice, pageable);
        Page<Product> productPage = productRepository.searchProducts(query, province, categoryId, minPrice, maxPrice, pageable);
        return productMapper.toPageProductTagResponse(productPage);
    }

    public ProductDetailResponse getByIdOrSlug(String idOrSlug) {
        Product product = productRepository.findByIdOrSlugAndStatus(idOrSlug, idOrSlug, ProductStatus.AVAILABLE)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Product not found", "product-e-01"));
        return productMapper.toProductDetailResponse(product);
    }


    public ProductGetBySellerResponse changeStatus(String productId, String status) {
        ProductStatus productStatus = ProductStatus.valueOf(status);

        String sellerId = securityUtil.getCurrentUserId();
        Product product = productRepository
                .findByIdAndSellerId(productId, sellerId)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Product not found", "product-e-02"));

        ProductStatus currentStatus = product.getStatus();

        Map<ProductStatus, ProductStatus[]> allowedTransitions = Map.of(
                ProductStatus.PENDING, new ProductStatus[]{ProductStatus.HIDDEN},
                ProductStatus.AVAILABLE, new ProductStatus[]{ProductStatus.HIDDEN},
                ProductStatus.HIDDEN, new ProductStatus[]{ProductStatus.PENDING},
                ProductStatus.REJECTED, new ProductStatus[]{ProductStatus.PENDING, ProductStatus.HIDDEN},
                ProductStatus.EXPIRED, new ProductStatus[]{ProductStatus.PENDING, ProductStatus.HIDDEN}
        );

        if (currentStatus == ProductStatus.SOLD) {
            throw new AppException(HttpStatus.BAD_REQUEST,
                    "Cannot change status for product in SOLD state", "product-e-03");
        }

        if (allowedTransitions.containsKey(currentStatus)) {
            ProductStatus[] validNextStatuses = allowedTransitions.get(currentStatus);
            boolean isValidTransition = false;
            for (ProductStatus validStatus : validNextStatuses) {
                if (validStatus == productStatus) {
                    isValidTransition = true;
                    break;
                }
            }
            if (!isValidTransition) {
                throw new AppException(HttpStatus.BAD_REQUEST,
                        "Cannot set product to " + productStatus.name() + " from " + currentStatus.name(), "product-e-04");
            }
        } else {
            throw new AppException(HttpStatus.BAD_REQUEST,
                    "Invalid status transition", "product-e-09");
        }

        product.setStatus(productStatus);
        productRepository.save(product);

        return productMapper.toProductGetBySellerResponse(product);
    }

    public List<Product> getAvailableProductsByUser(String sellerId) {
        return productRepository.findBySellerIdAndStatus(sellerId, ProductStatus.AVAILABLE);
    }

}
