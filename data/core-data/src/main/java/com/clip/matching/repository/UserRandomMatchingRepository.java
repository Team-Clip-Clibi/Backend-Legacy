package com.clip.matching.repository;

import com.clip.matching.entity.UserRandomMatching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRandomMatchingRepository extends JpaRepository<UserRandomMatching, Long> {

    @Query("""
            select u
            from UserRandomMatching u
            join fetch u.randomMatching
            where u.user.id = :userId
            and u.randomMatching.meetingTime >= :date
            order by u.randomMatching.meetingTime asc
            """)
    List<UserRandomMatching> findUserRandomMatching(Long userId, LocalDateTime date);

    @Query("""
            select u
            from UserRandomMatching u
            join fetch u.randomMatching
            where u.user.id = :userId
            and u.randomMatching.meetingTime >= :dateTime
            order by u.randomMatching.meetingTime desc
            limit 1
            """)
    Optional<UserRandomMatching> findLatestUserRandomMatching(@Param("userId") long userId, @Param("dateTime") LocalDateTime dateTime);

    @Query("""
            select u
            from UserRandomMatching u
            join fetch u.user
            where u.randomMatching.id = :randomMatchingId
            """)
    List<UserRandomMatching> findUserRandomMatching(@Param("randomMatchingId") long randomMatchingId);

    @Transactional
    @Modifying
    @Query("update UserRandomMatching u set u.isCheckedMatchingStart = true where u.user.id = :userId and u.id = :id")
    void updateStatusChecked(@Param("userId") long userId, @Param("id") long userRandomMatchingId);
}
