package com.clip.matching.repository;

import com.clip.matching.entity.MatchingStatus;
import com.clip.matching.entity.UserOneThingMatching;
import com.clip.order.entity.OneThingOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserOneThingMatchingRepository extends JpaRepository<UserOneThingMatching, Long> {

    @Query("""
            select u
            from UserOneThingMatching u
            join fetch u.oneThingMatching
            where u.user.id = :userId
            and u.oneThingMatching.meetingTime >= :date
            order by u.oneThingMatching.meetingTime asc
            """)
    List<UserOneThingMatching> findUserOneThingMatching(@Param("userId") Long userId, @Param("date") LocalDateTime date);

    @Query("""
            select u
            from UserOneThingMatching u
            join fetch u.oneThingMatching
            where u.user.id = :userId
            and u.oneThingMatching.meetingTime >= :dateTime
            order by u.oneThingMatching.meetingTime
            limit 1
            """)
    Optional<UserOneThingMatching> findLatestUserOneThingMatching(@Param("userId") long userId, @Param("dateTime") LocalDateTime dateTime);

    @Query("""
            select u
            from UserOneThingMatching u
            join fetch u.user
            where u.oneThingMatching.id = :oneThingMatchingId
            """)
    List<UserOneThingMatching> findUserOneThingMatching(@Param("oneThingMatchingId") long oneThingMatchingId);

    @Transactional
    @Modifying
    @Query("update UserOneThingMatching u set u.isCheckedMatchingStart = true where u.user.id = :userId and u.id = :id")
    void updateStatusChecked(@Param("userId") long userId, @Param("id") long userOnethingMatchingId);

    @Query("""
            select u
            from UserOneThingMatching u
            where u.user.id = :userId
            and u.matchingStatus = :matchingStatus
            """)
    List<UserOneThingMatching> findConfirmedUserOneThingMatching(@Param("userId") long userId, @Param("matchingStatus") MatchingStatus matchingStatus);

    //todo / to.세은 / 2025-05-18 / OnethingOrder entity 변경으로 인한 쿼리 수정, 정상 동작하는지 확인해주세요!
    @Query("""
            select u
            from UserOneThingMatching u
            where u.user.id = :userId
            and u.matchingStatus = :matchingStatus
            and u.oneThingOrder.status = :oneThingOrderStatus
            """)
    List<UserOneThingMatching> findAppliedUserOneThingMatching(@Param("userId") long userId, @Param("matchingStatus") MatchingStatus matchingStatus
    , @Param("oneThingOrderStatus") OneThingOrderStatus oneThingOrderStatus);

    @Transactional
    @Modifying
    @Query("delete from UserOneThingMatching u where u.user.id = :userId ")
    void deleteOneThingMatching(@Param("userId") long userId);
}
