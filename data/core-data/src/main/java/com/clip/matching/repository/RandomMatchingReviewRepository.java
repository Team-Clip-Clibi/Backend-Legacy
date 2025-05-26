package com.clip.matching.repository;

import com.clip.matching.entity.RandomMatchingReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface RandomMatchingReviewRepository extends JpaRepository<RandomMatchingReview, Long> {

    @Query("select o from RandomMatchingReview o where o.user.id = :userId and o.randomMatching.id = :randomMatchingId")
    Optional<RandomMatchingReview> findRandomMatchingReview(@Param("userId") final Long userId, @Param("randomMatchingId") final Long randomMatchingId);

    @Transactional
    @Modifying
    @Query("delete from RandomMatchingReview o where o.user.id = :userId ")
    void deleteRandomMatchingReview(@Param("userId") long userId);
}
