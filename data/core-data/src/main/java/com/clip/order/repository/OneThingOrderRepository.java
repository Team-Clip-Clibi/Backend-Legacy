package com.clip.order.repository;

import com.clip.order.entity.OneThingOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface OneThingOrderRepository extends JpaRepository<OneThingOrder, Long> {

    @Query("select o from OneThingOrder o where o.user.id = :userId and o.orderId = :orderId")
    Optional<OneThingOrder> findOneThingOrder(@Param("userId") long userId, @Param("orderId") UUID orderId);
}
