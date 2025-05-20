package com.clip.price.repository;

import com.clip.price.entity.RandomPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RandomPriceRepository extends JpaRepository<RandomPrice, Long> {
    @Query("select r from RandomPrice r " +
            "where r.PriceType = com.clip.price.entity.OneThingPriceType.BASIC " +
            "order by r.createdAt desc " +
            "limit 1")
    Optional<RandomPrice> findBasicPrice();
}
