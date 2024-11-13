package com.secondhandmarket.repository;

import com.secondhandmarket.enums.ReviewStatus;
import com.secondhandmarket.enums.ReviewType;
import com.secondhandmarket.model.Product;
import com.secondhandmarket.model.Review;
import com.secondhandmarket.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, String> {
    Optional<Review> findByReviewerAndSellerAndProductAndStatusAndReviewType
            (User reviewer, User seller, Product product, ReviewStatus status, ReviewType reviewType);
    List<Review> findBySeller(User seller);
    List<Review> findByReviewer(User reviewer);
}
