package com.secondhandmarket.repository;

import com.secondhandmarket.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, String> {
}
