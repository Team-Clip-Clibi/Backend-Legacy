package com.clip.price.repository;

import com.clip.price.entity.OneThingPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OneThingPriceRepository extends JpaRepository<OneThingPrice, Long> {
    @Query("select o from OneThingPrice o " +
            "where o.PriceType = com.clip.price.entity.OneThingPriceType.BASIC " +
            "order by o.createdAt desc " +
            "limit 1")
    Optional<OneThingPrice> findBasicPrice();
}
