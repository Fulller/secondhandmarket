package com.secondhandmarket.repository;

import com.secondhandmarket.enums.ProductStatus;
import com.secondhandmarket.model.Address;
import com.secondhandmarket.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    Optional<Product> findByIdAndSellerId(String Id, String sellerId);

    List<Product> findBySellerId(String sellerId);

    List<Product> findBySellerIdAndStatus(String sellerId, ProductStatus status);

    boolean existsByAddress(Address address);

    Page<Product> findAllByStatus(ProductStatus status, Pageable pageable);

    @Query("SELECT DISTINCT p FROM Product p " +
            "LEFT JOIN p.productAttributes pa " +
            "WHERE p.status = 'AVAILABLE' AND " +
            "(:province IS NULL OR p.address.province = :province) AND " +
            "(:categoryId IS NULL OR p.category.id = :categoryId) AND " +
            "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
            "(LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(pa.value) LIKE LOWER(CONCAT('%', :query, '%'))) ")
    Page<Product> searchProducts(@Param("query") String query,
                                 @Param("province") String province,
                                 @Param("categoryId") String categoryId,
                                 @Param("minPrice") Double minPrice,
                                 @Param("maxPrice") Double maxPrice,
                                 Pageable pageable);

    Optional<Product> findByIdOrSlugAndStatus(String id, String slug, ProductStatus status);

<<<<<<< HEAD
    @Query("SELECT p FROM Product p WHERE p.status = 'PENDING'")
    Page<Product> findAllByStatusIsPending(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.status = 'AVAILABLE'")
    Page<Product> findAllByStatusIsAvailable(Pageable pageable);
=======
>>>>>>> feature/be11
}
