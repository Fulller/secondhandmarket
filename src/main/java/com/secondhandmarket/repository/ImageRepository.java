package com.secondhandmarket.repository;

import com.secondhandmarket.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, String> {
    void deleteByProductId(String productId);

    void deleteAllByIdNotInAndProductId(List<String> imageIds, String productId);
}
