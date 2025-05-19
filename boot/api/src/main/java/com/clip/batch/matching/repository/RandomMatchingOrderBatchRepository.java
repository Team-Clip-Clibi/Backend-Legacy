package com.clip.batch.matching.repository;

import com.clip.order.repository.RandomOrderRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface RandomMatchingOrderBatchRepository extends RandomOrderRepository {

    @Transactional
    @Query("delete from RandomOrder r where r.expiredAt < :now")
    void deleteExpiredOrder(@Param("now") LocalDateTime now);
}
