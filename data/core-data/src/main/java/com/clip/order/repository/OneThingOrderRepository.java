package com.clip.order.repository;

import com.clip.order.entity.OneThingOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OneThingOrderRepository extends JpaRepository<OneThingOrder, Long> {

    @Query("select o from OneThingOrder o where o.user.id = :userId and o.oneThingMatching.id = :oneThingMatchingId")
    Optional<OneThingOrder> findOneThingOrder(@Param("userId") long userId, @Param("oneThingMatchingId") long oneThingMatchingId);
}
