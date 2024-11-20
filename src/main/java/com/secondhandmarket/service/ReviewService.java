package com.secondhandmarket.service;

import com.secondhandmarket.dto.review.PostFeedback;
import com.secondhandmarket.dto.review.PostReviewRequest;
import com.secondhandmarket.dto.review.ReviewResponse;
import com.secondhandmarket.dto.review.ReviewUpdateRequest;
import com.secondhandmarket.enums.ReviewStatus;
import com.secondhandmarket.enums.ReviewType;
import com.secondhandmarket.exception.AppException;
import com.secondhandmarket.mapper.ReviewMapper;
import com.secondhandmarket.model.Product;
import com.secondhandmarket.model.Review;
import com.secondhandmarket.model.User;
import com.secondhandmarket.repository.ProductRepository;
import com.secondhandmarket.repository.ReviewRepository;
import com.secondhandmarket.repository.UserRepository;
import com.secondhandmarket.security.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SecurityUtil securityUtil;
    @Autowired
    private ReviewMapper reviewMapper;

    @Transactional
    public ReviewResponse postReview(String reviewId, PostReviewRequest request){
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new AppException("Review not found"));
        review.setComment(request.getComment());
        review.setRating(request.getRating());
        if(request.getImage() == null) {
            review.setImage("");
        }else {
            review.setImage(request.getImage());
        }
        review.setStatus(ReviewStatus.PUBLIC);

        reviewRepository.save(review);
        updateRating(review.getSeller().getId());
        return reviewMapper.toReviewResponse(review);
    }
    private void updateRating(String sellerId) {
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "user not found"));

        List<Review> reviews = reviewRepository.findBySeller(seller);

        // Tính tổng số điểm từ tất cả các đánh giá hien co
        int totalRating = reviews.stream().mapToInt(Review::getRating).sum();

        // Tính tổng số lần đánh giá
        int numberOfRatings = reviews.size();
        Double averageRate = (double) (totalRating / numberOfRatings);

        seller.setRating(averageRate);
        userRepository.save(seller);
    }

    public ReviewResponse getReview(String id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new AppException("Review not found"));

        return reviewMapper.toReviewResponse(review);
    }

    public ReviewResponse updateReview(String id, ReviewUpdateRequest request) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "review not found"));

        User reviewer = securityUtil.getCurrentUser();
        if(!review.getReviewer().equals(reviewer)){
            throw new AppException(HttpStatus.FORBIDDEN, "You are not owner of review");
        }
        review.setImage(request.getImage());
        review.setComment(request.getComment());
        review.setRating(request.getRating());

        reviewRepository.save(review);

        updateRating(review.getSeller().getId());
        return reviewMapper.toReviewResponse(review);
    }

    public void changeStatusReview(String id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "review not found"));
        User reviewer = securityUtil.getCurrentUser();
        if(!review.getReviewer().equals(reviewer)){
            throw new AppException(HttpStatus.FORBIDDEN, "You are not owner of review");
        }
        review.setStatus(ReviewStatus.HIDDEN);
        reviewRepository.save(review);
    }

    public List<ReviewResponse> getBySeller(ReviewStatus status) {
        User seller = securityUtil.getCurrentUser();
        List<Review> reviews;
        if(status == null) {
            reviews = reviewRepository.findBySeller(seller);
        }else {
            reviews = reviewRepository.findByStatusAndSeller(status, seller);
        }
        List<ReviewResponse> reviewResponses = reviewMapper.toReviewResponses(reviews);
        return reviewResponses;
    }

    public List<ReviewResponse> getByReviewer(ReviewStatus status) {
        User reviewer = securityUtil.getCurrentUser();
        List<Review> reviews;
        if(status == null) {
            reviews = reviewRepository.findByReviewer(reviewer);
        }else {
            reviews = reviewRepository.findByStatusAndReviewer(status, reviewer);
        }
        List<ReviewResponse> reviewResponses = reviewMapper.toReviewResponses(reviews);
        return reviewResponses;
    }

    public ReviewResponse postFeedback(String reviewId, PostFeedback request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "review not found"));
        User seller = securityUtil.getCurrentUser();
        if(!review.getProduct().getSeller().equals(seller)){
            throw new AppException(HttpStatus.FORBIDDEN, "You are not owner of review");
        }
        review.setFeedbackFromSeller(request.getFeedback());
        reviewRepository.save(review);
        return reviewMapper.toReviewResponse(review);
    }

    public List<ReviewResponse> getReviewShop(String id,ReviewStatus status) {
        User shop = userRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "user not found"));
        List<Review> reviews;
        if(status == null) {
            reviews = reviewRepository.findBySeller(shop);
        }   else{
            reviews = reviewRepository.findBySellerAndStatus(shop, ReviewStatus.PUBLIC);
        }
        return reviewMapper.toReviewResponses(reviews);
    }
}
