package com.secondhandmarket.repository;

import com.secondhandmarket.enums.ReviewStatus;
import com.secondhandmarket.enums.ReviewType;
import com.secondhandmarket.model.Product;
import com.secondhandmarket.model.Review;
import com.secondhandmarket.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, String> {
    Optional<Review> findByReviewerAndSellerAndProductAndStatusAndReviewType
            (User reviewer, User seller, Product product, ReviewStatus status, ReviewType reviewType);
    List<Review> findBySellerAndStatus(User seller, ReviewStatus status);
    List<Review> findBySeller(User seller);
    List<Review> findByReviewer(User reviewer);
    List<Review> findByStatusAndReviewer(ReviewStatus status, User reviewer);
    List<Review> findByStatusAndSeller(ReviewStatus status, User seller);
}
