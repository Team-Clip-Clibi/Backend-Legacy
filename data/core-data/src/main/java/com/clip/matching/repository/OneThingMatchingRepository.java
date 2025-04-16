package com.clip.matching.repository;

import com.clip.matching.entity.OneThingMatching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface OneThingMatchingRepository extends JpaRepository<OneThingMatching, Long> {
    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM OneThingMatching rm WHERE rm.id = :oneThingMatchingId")
    void deleteOneThingMatching(@Param("oneThingMatchingId")Long oneThingMatchingId);
}
