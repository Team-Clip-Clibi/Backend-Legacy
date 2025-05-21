package com.clip.matching.repository;

import com.clip.matching.entity.OneThingMatchingReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface OneThingMatchingReviewRepository extends JpaRepository<OneThingMatchingReview, Long> {

    @Query("select o from OneThingMatchingReview o where o.user.id = :userId and o.oneThingMatching.id = :oneThingMatchingId")
    Optional<OneThingMatchingReview> findOneThingMatchingReview(@Param("userId") final Long userId, @Param("oneThingMatchingId") final Long oneThingMatchingId);

    @Transactional
    @Modifying
    @Query("delete from OneThingMatchingReview o where o.user.id = :userId ")
    void deleteOneThingMatchingReview(@Param("userId") long userId);
}
