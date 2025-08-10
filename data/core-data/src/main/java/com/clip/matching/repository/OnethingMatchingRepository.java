package com.clip.matching.repository;

import com.clip.matching.entity.OneThingMatching;
import com.clip.matching.entity.OnethingDistrict;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface OnethingMatchingRepository extends JpaRepository<OneThingMatching, Long> {
    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM OneThingMatching rm WHERE rm.id = :oneThingMatchingId")
    void deleteOneThingMatching(@Param("oneThingMatchingId")Long oneThingMatchingId);

    @Query("SELECT o " +
            "FROM OneThingMatching o " +
            "WHERE (:startDateTime IS NULL OR o.dateTime >= :startDateTime) " +
            "AND (:endDateTime IS NULL OR o.dateTime < :endDateTime) " +
            "AND (:district IS NULL OR o.onethingDistrict = :district) " +
            "ORDER BY o.id")
    Slice<OneThingMatching> findMatchingList(@Param("startDateTime") LocalDateTime startDateTime,
                                             @Param("endDateTime") LocalDateTime endDateTime,
                                             @Param("district") OnethingDistrict district,
                                             Pageable page);

    @Query("SELECT o FROM OneThingMatching o WHERE o.id IN :ids")
    List<OneThingMatching> findAllByIds(@Param("ids") List<Long> ids);

    @Query("SELECT o FROM OneThingMatching o WHERE o.questionSheet IS NOT NULL ORDER BY o.id DESC ")
    Slice<OneThingMatching> findMatchingQuestionIsNotNullList(PageRequest of);

    @Query("SELECT o FROM OneThingMatching o WHERE o.questionSheet IS NULL ORDER BY o.id DESC ")
    Slice<OneThingMatching> findMatchingQuestionIsNullList(PageRequest of);
}
