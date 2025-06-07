package com.clip.batch.matching.repository;

import com.clip.order.entity.RandomOrder;
import com.clip.order.repository.RandomOrderRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface RandomMatchingOrderBatchRepository extends RandomOrderRepository {

    @Query("""
            select r from RandomOrder r
            join fetch r.randomMatching
            where r.expiredAt < :now
            """)
    List<RandomOrder> findExpiredOrders(@Param("now") LocalDateTime now);
}
