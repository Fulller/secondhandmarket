package com.secondhandmarket.controller;

import com.secondhandmarket.dto.api.ApiResponse;
import com.secondhandmarket.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/review")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

//    @PostMapping("/post")
//    ResponseEntity<ApiResponse<>>

}
