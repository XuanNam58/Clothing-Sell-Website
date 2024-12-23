package com.example.clothing_sell_website.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.clothing_sell_website.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    @Query("FROM Review r WHERE r.product.productId = :productId ")
    List<Review> getReviewByPro(@Param("productId") String productId);

    @Query("FROM Review r WHERE r.product.productId = :productId AND r.customer.customerId = :customerId ")
    Review getReviewByProCus(@Param("productId") String productId, @Param("customerId") String customer);
}
