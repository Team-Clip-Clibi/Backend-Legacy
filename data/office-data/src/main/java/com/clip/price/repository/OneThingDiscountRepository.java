package com.clip.price.repository;

import com.clip.price.entity.OneThingDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OneThingDiscountRepository extends JpaRepository<OneThingDiscount, Long> {
    @Query("select o from OneThingDiscount o " +
            "where o.discountType = com.clip.price.entity.DiscountType.BASE " +
            "order by o.createdAt desc " +
            "limit 1")
    Optional<OneThingDiscount> findBaseDiscount();
}
