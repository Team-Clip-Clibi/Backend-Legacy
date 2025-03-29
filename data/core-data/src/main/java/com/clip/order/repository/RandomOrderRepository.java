package com.clip.order.repository;

import com.clip.order.entity.RandomOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RandomOrderRepository extends JpaRepository<RandomOrder, Long> {

    @Query("select r from RandomOrder r where r.user.id = :userId and r.randomMatching.id = :randomMatchingId")
    Optional<RandomOrder> findRandomOrder(@Param("userId") long userId, @Param("randomMatchingId") long randomMatchingId);
}
