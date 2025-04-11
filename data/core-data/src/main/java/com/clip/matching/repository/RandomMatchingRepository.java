package com.clip.matching.repository;

import com.clip.matching.entity.RandomMatching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface RandomMatchingRepository extends JpaRepository<RandomMatching, Long> {
    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM RandomMatching rm WHERE rm.id = :randomMatchingId")
    void deleteRandomMatching(@Param("randomMatchingId")Long randomMatchingId);
}
