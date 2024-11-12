package com.secondhandmarket.mapper;

import com.secondhandmarket.dto.review.ReviewResponse;
import com.secondhandmarket.model.Product;
import com.secondhandmarket.model.Review;
import com.secondhandmarket.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    ReviewResponse.UserDTO toUserDTO(User user);
    ReviewResponse.ProductDTO toProductDTO(Product product);
    ReviewResponse toReviewResponse(Review review);
    List<ReviewResponse> toReviewResponses(List<Review> reviews);
}
