package com.secondhandmarket.controller;

import com.secondhandmarket.dto.api.ApiResponse;
import com.secondhandmarket.dto.api.PagedResponse;
import com.secondhandmarket.dto.product.*;
import com.secondhandmarket.model.Product;
import com.secondhandmarket.service.ProductService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ProductController {
    ProductService productService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProductPostingResponse>> post(@RequestBody @Valid ProductPostingRequest productPostingRequest) {
        ApiResponse<ProductPostingResponse> apiResponse = ApiResponse.<ProductPostingResponse>builder()
                .code("product-s-01")
                .message("Posting to sell product successfully")
                .data(productService.post(productPostingRequest))
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("/seller")
    public ResponseEntity<ApiResponse<List<ProductGetBySellerResponse>>> getAllBySeller() {
        ApiResponse<List<ProductGetBySellerResponse>> apiResponse = ApiResponse.<List<ProductGetBySellerResponse>>builder()
                .code("product-s-02")
                .message("Get all product by seller successfully")
                .data(productService.getAllBySeller())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/seller/{productId}")
    public ResponseEntity<ApiResponse<ProductGetBySellerResponse>> getOneBySeller(@PathVariable String productId) {
        ApiResponse<ProductGetBySellerResponse> apiResponse = ApiResponse.<ProductGetBySellerResponse>builder()
                .code("product-s-03")
                .message("Get product by seller successfully")
                .data(productService.getOneBySeller(productId))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/home")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<PagedResponse<ProductTagResponse>>> getHomeProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<ProductTagResponse> pagedProducts = productService.getHomeProducts(page, size);

        PagedResponse<ProductTagResponse> pagedResponseProducts = PagedResponse.<ProductTagResponse>builder()
                .totalPages(pagedProducts.getTotalPages())
                .page(pagedProducts.getNumber())
                .totalElements(pagedProducts.getTotalElements())
                .content(pagedProducts.getContent())
                .build();

        ApiResponse<PagedResponse<ProductTagResponse>> apiResponse = ApiResponse.<PagedResponse<ProductTagResponse>>builder()
                .code("product-s-04")
                .message("Get home products successfully")
                .data(pagedResponseProducts)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/search")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<PagedResponse<ProductTagResponse>>> getByIdOrSlug(
            @RequestParam String q,
            @RequestParam(required = false) String province,
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<ProductTagResponse> pagedProducts = productService.searchProducts(q, province, categoryId, minPrice, maxPrice, page, size);

        PagedResponse<ProductTagResponse> pagedResponseProducts = PagedResponse.<ProductTagResponse>builder()
                .totalPages(pagedProducts.getTotalPages())
                .page(pagedProducts.getNumber())
                .totalElements(pagedProducts.getTotalElements())
                .content(pagedProducts.getContent())
                .build();

        ApiResponse<PagedResponse<ProductTagResponse>> apiResponse = ApiResponse.<PagedResponse<ProductTagResponse>>builder()
                .code("product-s-05")
                .message("Search products successfully")
                .data(pagedResponseProducts)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/{idOrSlug}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponse<ProductDetailResponse>> getByIdOrSlug(@PathVariable String idOrSlug) {
        ApiResponse<ProductDetailResponse> apiResponse = ApiResponse.<ProductDetailResponse>builder()
                .code("product-s-06")
                .message("Get product by id or slug successfully")
                .data(productService.getByIdOrSlug(idOrSlug))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PutMapping("/seller/{productId}/change-status/{status}")
    public ResponseEntity<ApiResponse<ProductGetBySellerResponse>> changeStatus(@PathVariable String productId, @PathVariable String status) {
        ApiResponse<ProductGetBySellerResponse> apiResponse = ApiResponse.<ProductGetBySellerResponse>builder()
                .code("product-s-07")
                .message("Changed product status successfully")
                .data(productService.changeStatus(productId, status))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }


    @PutMapping("/seller/{productId}")
    public ResponseEntity<ApiResponse<ProductGetBySellerResponse>> updateProduct(
            @PathVariable String productId,
            @RequestBody @Valid ProductUpdateRequest productUpdateRequest) {
        productUpdateRequest.setProductId(productId);
        ApiResponse<ProductGetBySellerResponse> apiResponse = ApiResponse.<ProductGetBySellerResponse>builder()
                .code("product-s-08")
                .message("Product updated successfully")
                .data(productService.updateProduct(productUpdateRequest))
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/seller/{sellerId}/available")
    public ResponseEntity<ApiResponse<List<Product>>> getAvailableProductsByUser(@PathVariable String sellerId) {
        List<Product> availableProducts = productService.getAvailableProductsByUser(sellerId);

        ApiResponse<List<Product>> apiResponse = ApiResponse.<List<Product>>builder()
                .code("product-s-10")
                .message("Get available products by user successfully")
                .data(availableProducts)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}
