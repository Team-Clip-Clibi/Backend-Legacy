package com.clip.matching.repository;

import com.clip.matching.entity.RandomDistrict;
import com.clip.matching.entity.RandomMatching;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface RandomMatchingRepository extends JpaRepository<RandomMatching, Long> {
    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM RandomMatching rm WHERE rm.id = :randomMatchingId")
    void deleteRandomMatching(@Param("randomMatchingId")Long randomMatchingId);

    @Query("SELECT r " +
            "FROM RandomMatching r " +
            "WHERE r.dateTime >= :startDateTime " +
            "AND r.dateTime < :endDateTime " +
            "AND r.randomDistrict = :district")
    Slice<RandomMatching> findMatchingList(@Param("startDateTime") LocalDateTime startDateTime,
                                           @Param("endDateTime") LocalDateTime endDateTime,
                                           @Param("district") RandomDistrict district,
                                           PageRequest page);

    @Query("SELECT r FROM RandomMatching r WHERE r.id IN :ids")
    List<RandomMatching> findAllByIds(@Param("ids") List<Long> ids);

    @Query("SELECT r FROM RandomMatching r WHERE r.questionSheet IS NOT NULL ORDER BY r.id DESC ")
    Slice<RandomMatching> findMatchingListFetchQuestion(PageRequest of);
}
