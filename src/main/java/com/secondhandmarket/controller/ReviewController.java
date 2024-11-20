package com.secondhandmarket.controller;

import com.secondhandmarket.dto.api.ApiResponse;
import com.secondhandmarket.dto.review.PostFeedback;
import com.secondhandmarket.dto.review.PostReviewRequest;
import com.secondhandmarket.dto.review.ReviewResponse;
import com.secondhandmarket.dto.review.ReviewUpdateRequest;
import com.secondhandmarket.enums.OrderStatus;
import com.secondhandmarket.enums.ReviewStatus;
import com.secondhandmarket.model.Review;
import com.secondhandmarket.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
   final private ReviewService reviewService;

    @PostMapping("/{id}/post")
    ResponseEntity<ApiResponse<ReviewResponse>> post(
            @PathVariable String id,
            @RequestBody @Valid PostReviewRequest request) {
        ApiResponse<ReviewResponse> apiResponse = ApiResponse.<ReviewResponse>builder()
                .code("review-s-01")
                .message("post review success")
                .data(reviewService.postReview(id, request))
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
    //get review
    @GetMapping("/{id}")
    ResponseEntity<ApiResponse<ReviewResponse>> getReview(@PathVariable String id){
        ApiResponse<ReviewResponse> apiResponse = ApiResponse.<ReviewResponse>builder()
                .code("review-s-02")
                .message("get review success")
                .data(reviewService.getReview(id))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    //update review
    @PutMapping("/{id}")
    ResponseEntity<ApiResponse<ReviewResponse>> updateReview(
            @PathVariable String id,
            @RequestBody ReviewUpdateRequest request) {
        ApiResponse<ReviewResponse> apiResponse = ApiResponse.<ReviewResponse>builder()
                .code("review-s-03")
                .message("update review success")
                .data(reviewService.updateReview(id, request))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    //delete Review
    @PutMapping("/{id}/change-status")
    ResponseEntity<ApiResponse> changeStatusReview(@PathVariable String id){
        reviewService.changeStatusReview(id);
        ApiResponse apiResponse = ApiResponse.builder()
                .code("review-s-04")
                .message("Review change hidden status")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    //get review of seller
    @GetMapping("/seller")
    ResponseEntity<ApiResponse<List<ReviewResponse>>> getSellerReview(
            @RequestParam(value = "status", required = false) ReviewStatus status
    ){
        ApiResponse<List<ReviewResponse>> apiResponse = ApiResponse.<List<ReviewResponse>>builder()
                .code("review-s-05")
                .message("get review of seller success")
                .data(reviewService.getBySeller(status))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    //get review of shop
    @GetMapping("/seller/{id}")
    ResponseEntity<ApiResponse<List<ReviewResponse>>> getShopReview(
            @PathVariable String id,
            @RequestParam(value = "status", required = false) ReviewStatus status
    ){
        ApiResponse<List<ReviewResponse>> apiResponse = ApiResponse.<List<ReviewResponse>>builder()
                .code("review-s-07")
                .message("Get review for shop")
                .data(reviewService.getReviewShop(id, status))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    //get review of reviewer
    @GetMapping("/reviewer")
    ResponseEntity<ApiResponse<List<ReviewResponse>>> getReviewerReview(
            @RequestParam(value = "status", required = false) ReviewStatus status
    ){
        ApiResponse<List<ReviewResponse>> apiResponse = ApiResponse.<List<ReviewResponse>>builder()
                .code("review-s-05")
                .message("get review of reviewer success")
                .data(reviewService.getByReviewer(status))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    //post feedback
    @PostMapping("/{id}/feedback")
    ResponseEntity<ApiResponse<ReviewResponse>> postFeedback(
            @PathVariable String id,
            @RequestBody PostFeedback request) {
        ApiResponse<ReviewResponse> apiResponse = ApiResponse.<ReviewResponse>builder()
                .code("review-s-06")
                .message("post review feedback success")
                .data(reviewService.postFeedback(id, request))
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

}
