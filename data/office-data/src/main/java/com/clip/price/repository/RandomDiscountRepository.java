package com.clip.price.repository;

import com.clip.price.entity.RandomDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RandomDiscountRepository extends JpaRepository<RandomDiscount, Long> {
    @Query("select r from RandomDiscount r " +
            "where r.discountType = com.clip.price.entity.DiscountType.BASE " +
            "order by r.createdAt desc " +
            "limit 1")
    Optional<RandomDiscount> findBaseDiscount();
}
