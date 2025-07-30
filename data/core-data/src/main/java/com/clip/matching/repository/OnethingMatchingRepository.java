package com.clip.matching.repository;

import com.clip.matching.entity.OneThingMatching;
import com.clip.matching.entity.OnethingDistrict;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface OnethingMatchingRepository extends JpaRepository<OneThingMatching, Long> {
    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM OneThingMatching rm WHERE rm.id = :oneThingMatchingId")
    void deleteOneThingMatching(@Param("oneThingMatchingId")Long oneThingMatchingId);

    @Query("SELECT o " +
            "FROM OneThingMatching o " +
            "WHERE o.dateTime >= :startDateTime " +
            "AND o.dateTime < :endDateTime " +
            "AND o.onethingDistrict = :district " +
            "ORDER BY o.id")
    Slice<OneThingMatching> findMatchingList(@Param("startDateTime") LocalDateTime startDateTime,
                                             @Param("endDateTime") LocalDateTime endDateTime,
                                             @Param("district") OnethingDistrict district,
                                             Pageable page);
}
