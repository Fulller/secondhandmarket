package com.secondhandmarket.mapper;

import com.secondhandmarket.dto.product.*;
import com.secondhandmarket.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface ProductMapper {
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "productAttributes", ignore = true)
    Product toProduct(ProductPostingRequest request);

    @Mapping(target = "seller", source = "product.seller")
    ProductPostingResponse toProductPostingResponse(Product product);

    ProductGetBySellerResponse toProductGetBySellerResponse(Product product);

    List<ProductGetBySellerResponse> toListProductGetBySellerResponse(List<Product> product);

    @Mapping(source = "seller", target = "seller")
    ProductTagResponse toProductTagResponse(Product product);

    @Mapping(source = "seller", target = "seller")
    ProductDetailResponse toProductDetailResponse(Product product);

    default Page<ProductTagResponse> toPageProductTagResponse(Page<Product> productPage) {
        List<ProductTagResponse> productTagResponses = productPage.getContent().stream()
                .map(this::toProductTagResponse)
                .toList();
        return new PageImpl<>(productTagResponses, productPage.getPageable(), productPage.getTotalElements());
    }
}
