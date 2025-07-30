package com.clip.matching.repository;

import com.clip.matching.entity.OneThingMatching;
import com.clip.matching.entity.OneThingMatchingStatus;
import com.clip.matching.entity.OnethingDistrict;
import com.clip.matching.entity.UserOneThingMatching;
import com.clip.matching.repository.projection.FirstParticipantKeywordDto;
import com.clip.matching.repository.projection.MatchingParticipantCntDto;
import com.clip.order.entity.OneThingOrderStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserOneThingMatchingRepository extends JpaRepository<UserOneThingMatching, Long> {

    @Query("""
            select u
            from UserOneThingMatching u
            join fetch u.oneThingMatching
            where u.user.id = :userId
            and u.oneThingMatching.dateTime >= :date
            and u.matchingStatus = :matchingStatus
            order by u.oneThingMatching.dateTime asc
            """)
    List<UserOneThingMatching> findUserOneThingMatching(@Param("userId") Long userId, @Param("date") LocalDateTime date,
                                                        @Param("matchingStatus") OneThingMatchingStatus matchingStatus);

    @Query("""
            select u
            from UserOneThingMatching u
            where u.user.id = :userId
            and u.oneThingMatching.id = :onethingMatchingId
            and u.isEnded = false
            """)
    Optional<UserOneThingMatching> findNotEndedStatus(@Param("onethingMatchingId") long onethingMatchingId, @Param("userId") long userId);

    @Query("""
            select u
            from UserOneThingMatching u
            join fetch u.oneThingMatching
            where u.user.id = :userId
            and u.oneThingMatching.dateTime >= :dateTime
            and u.isEnded = false
            order by u.oneThingMatching.dateTime
            limit 1
            """)
    Optional<UserOneThingMatching> findLatestNotEndedStatus(@Param("userId") long userId, @Param("dateTime") LocalDateTime dateTime);

    @Query("select u " +
            "from UserOneThingMatching u " +
            "where u.user.id = :userId " +
                "and (u.matchingStatus = com.clip.matching.entity.OneThingMatchingStatus.APPLIED or u.matchingStatus = com.clip.matching.entity.OneThingMatchingStatus.CONFIRMED) " +
            "order by u.id limit 1")
    Optional<UserOneThingMatching> findLastestAppliedOrConfirmStatusUserOneThingMatching(@Param("userId") long userId);

    @Query("""
            select u
            from UserOneThingMatching u
            join fetch u.user
            where u.oneThingMatching.id = :oneThingMatchingId
            """)
    List<UserOneThingMatching> findUserOneThingMatching(@Param("oneThingMatchingId") long oneThingMatchingId);

    @Query("""
            select u
            from UserOneThingMatching u
            join fetch u.user
            where u.oneThingMatching.id = :oneThingMatchingId
            and u.user.firebaseToken is not null
            and u.user.isAllowNotify = true
            """)
    List<UserOneThingMatching> findAllUserOneThingMatchingsForNotification(@Param("oneThingMatchingId") long oneThingMatchingId);

    @Transactional
    @Modifying
    @Query("update UserOneThingMatching u set u.isEnded = true where u.user.id = :userId and u.id = :id")
    void updateStatusToEnded(@Param("userId") long userId, @Param("id") long userOnethingMatchingId);

    @Query("""
            select u
            from UserOneThingMatching u
            where u.user.id = :userId
            and u.matchingStatus = :matchingStatus
            """)
    List<UserOneThingMatching> findConfirmedUserOneThingMatching(@Param("userId") long userId, @Param("matchingStatus") OneThingMatchingStatus matchingStatus);

    //todo / to.세은 / 2025-05-18 / OnethingOrder entity 변경으로 인한 쿼리 수정, 정상 동작하는지 확인해주세요!
    @Query("""
            select u
            from UserOneThingMatching u
            where u.user.id = :userId
            and u.matchingStatus = :matchingStatus
            and u.oneThingOrder.status = :oneThingOrderStatus
            """)
    List<UserOneThingMatching> findAppliedUserOneThingMatching(@Param("userId") long userId, @Param("matchingStatus") OneThingMatchingStatus matchingStatus
    , @Param("oneThingOrderStatus") OneThingOrderStatus oneThingOrderStatus);

    @Transactional
    @Modifying
    @Query("delete from UserOneThingMatching u where u.user.id = :userId ")
    void deleteOneThingMatching(@Param("userId") long userId);

    @Query("""
            select u
            from UserOneThingMatching u
            join fetch u.preferredDates
            join fetch u.oneThingMatching
            join fetch u.user
            join fetch u.user.job
            where u.id = :id
            """)
    Optional<UserOneThingMatching> findUserOneThingMatchingWithMatchingInfo(@Param("id") long id);

    @Query("""
            select u
            from UserOneThingMatching u
            join fetch u.oneThingOrder
            join fetch u.oneThingOrder.price
            left join fetch u.oneThingOrder.tossPayment
            where u.id = :id
            """)
    Optional<UserOneThingMatching> findUserOneThingMatchingWithPaymentInfo(@Param("id") long id);

    @Modifying
    @Transactional
    @Query("""
            update UserOneThingMatching u
            set u.matchingStatus = :matchingStatus
            where u.id = :id
            """)
    void updateMatchingStatus(@Param("id") long id, @Param("matchingStatus") OneThingMatchingStatus matchingStatus);

    @Query("""
        select u
        from UserOneThingMatching u
        join fetch u.oneThingMatching
        join fetch u.user
        where u.id = :id
    """)
    Optional<UserOneThingMatching> findByIdWithOneThingMatchingAndUser(@Param("id") long id);

    @Query("""
        select u
        from UserOneThingMatching u
        join fetch u.user
        where u.oneThingMatching.id = :matchingId
    """)
    List<UserOneThingMatching> findRandomMatchingParticipants(@Param("matchingId") long matchingId);

    @Query("""
        select u
        from UserOneThingMatching u
        join fetch u.oneThingMatching
        where u.user.id = :userId
        and u.isReviewPopupDismissed = false
    """)
    List<UserOneThingMatching> findUserOneThingMatchingsReviewUnwritten(@Param("userId") long userId);

    @Modifying
    @Transactional
    @Query("""
            update UserOneThingMatching u
            set u.isReviewPopupDismissed = true
            where u.user.id = :userId
            and u.oneThingMatching.id = :matchingId
            """)
    void postponeOneThingMatchingReview(@Param("userId") long userId, @Param("matchingId") long matchingId);

    @Query("SELECT new com.clip.matching.repository.projection.MatchingParticipantCntDto(u.oneThingMatching.id, COUNT(u)) " +
            "FROM UserOneThingMatching u " +
            "WHERE u.oneThingMatching IN :onethingMatchings " +
            "GROUP BY u.oneThingMatching")
    List<MatchingParticipantCntDto> findParticipantCntIn(@Param("onethingMatchings") List<OneThingMatching> onethingMatchings);

    @Query("SELECT new com.clip.matching.repository.projection.FirstParticipantKeywordDto(u.id, u.oneThingKeyword) " +
            "FROM UserOneThingMatching u " +
            "WHERE u.oneThingMatching.id = :matchingId " +
            "AND u.id = ( SELECT MIN(u2.id) FROM UserOneThingMatching u2 WHERE u2.oneThingMatching = u.oneThingMatching)")
    List<FirstParticipantKeywordDto> findFirstParticipantKeywords(@Param("onethingMatchings") List<OneThingMatching> oneThingMatchings);

    @Query("SELECT u " +
            "FROM UserOneThingMatching u " +
//            "join fetch u.user.job.jobCategory " +
            "join fetch u.oneThingMatching " +
            "join u.preferredDates pd " +
            "WHERE u.oneThingMatching is NOT NULL AND u.oneThingDistrict = :onethingDistrict AND pd.date = :localDate " +
            "ORDER BY u.id")
    Slice<UserOneThingMatching> findAssignedParticipantsFetchUser(
            @Param("onethingDistrict") OnethingDistrict onethingDistrict,
            @Param("localDate") LocalDate localDate, PageRequest of);

    @Query("SELECT u " +
            "FROM UserOneThingMatching u " +
//            "join fetch u.user.job.jobCategory " +
            "join u.preferredDates pd " +
            "WHERE u.oneThingMatching is NULL AND u.oneThingDistrict = :onethingDistrict AND pd.date = :localDate " +
            "ORDER BY u.id")
    Slice<UserOneThingMatching> findUnassignedParticipantsFetchUser(
            @Param("onethingDistrict") OnethingDistrict onethingDistrict,
            @Param("localDate") LocalDate localDate, PageRequest of);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM UserOneThingMatching u WHERE u.id IN :ids")
    List<UserOneThingMatching> findByIdsForUpdate(@Param("ids") List<Long> ids);
}
