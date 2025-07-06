package com.clip.matching.repository;

import com.clip.matching.entity.RandomMatchingStatus;
import com.clip.matching.entity.UserRandomMatching;
import com.clip.order.entity.RandomOrderStatus;
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
            and u.matchingStatus = :matchingStatus
            order by u.randomMatching.meetingTime asc
            """)
    List<UserRandomMatching> findUserRandomMatching(Long userId, LocalDateTime date,
                                                    RandomMatchingStatus matchingStatus);

    @Query("""
            select u
            from UserRandomMatching u
            join fetch u.randomMatching
            where u.user.id = :userId
            and u.randomMatching.meetingTime >= :dateTime
            and u.isEnded = false
            order by u.randomMatching.meetingTime
            limit 1
            """)
    Optional<UserRandomMatching> findLatestNotEndedStatus(@Param("userId") long userId, @Param("dateTime") LocalDateTime dateTime);

    @Query("select u " +
            "from UserRandomMatching u " +
            "where u.user.id = :userId " +
                "and (u.matchingStatus = com.clip.matching.entity.RandomMatchingStatus.APPLIED or u.matchingStatus = com.clip.matching.entity.RandomMatchingStatus.CONFIRMED) " +
            "order by u.id limit 1")
    Optional<UserRandomMatching> findLastestAppliedOrConfirmStatusUserRandomMatching(@Param("userId") long userId);

    @Query("""
            select u
            from UserRandomMatching u
            join fetch u.user
            where u.randomMatching.id = :randomMatchingId
            """)
    List<UserRandomMatching> findUserRandomMatching(@Param("randomMatchingId") long randomMatchingId);

    @Query("""
            select u
            from UserRandomMatching u
            join fetch u.user
            where u.randomMatching.id = :randomMatchingId
            and u.user.firebaseToken is not null
            and u.user.isAllowNotify = true
            """)
    List<UserRandomMatching> findAllUserRandomMatchingsForNotification(@Param("randomMatchingId") long randomMatchingId);

    @Transactional
    @Modifying
    @Query("update UserRandomMatching u set u.isEnded = true where u.user.id = :userId and u.id = :id")
    void updateStatusToEnded(@Param("userId") long userId, @Param("id") long userRandomMatchingId);

    @Query("""
            select u
            from UserRandomMatching u
            where u.user.id = :userId
            and u.matchingStatus = :matchingStatus
            """)
    List<UserRandomMatching> findConfirmedUserRandomMatching(@Param("userId") long userId, @Param("matchingStatus") RandomMatchingStatus matchingStatus);

    @Query("""
            select u
            from UserRandomMatching u
            join RandomOrder r
            on u.randomMatching.id = r.randomMatching.id
            where u.user.id = :userId
            and u.matchingStatus = :matchingStatus
            and r.status = :randomOrderStatus
            """)
    List<UserRandomMatching> findAppliedUserRandomMatching(@Param("userId") long userId, @Param("matchingStatus") RandomMatchingStatus matchingStatus
            , @Param("randomOrderStatus") RandomOrderStatus randomOrderStatus);

    @Transactional
    @Modifying
    @Query("delete from UserRandomMatching u where u.user.id = :userId")
    void deleteRandomMatching(@Param("userId") long userId);

    @Query("""
            select u
            from UserRandomMatching u
            where u.user.id = :userId
            and u.randomMatching.meetingTime = :meetingTime
            and u.matchingStatus = :matchingStatus
            """)
    Optional<UserRandomMatching> findUserRandomMatching(@Param("userId") long userId, @Param("meetingTime") LocalDateTime meetingTime,
                                                        @Param("matchingStatus") RandomMatchingStatus matchingStatus);

    @Query("""
            select u
            from UserRandomMatching u
            where u.user.id = :userId
            and u.randomOrder.id = :randomOrderId
            """)
    Optional<UserRandomMatching> findUserRandomMatching(@Param("userId") long userId, @Param("randomOrderId") Long randomOrderId);

    @Query("""
            select u
            from UserRandomMatching u
            join fetch u.randomMatching
            join fetch u.user
            join fetch u.user.job
            join fetch u.randomOrder
            join fetch u.randomOrder.price
            join fetch u.randomOrder.tossPayment
            where u.id = :id
            """)
    Optional<UserRandomMatching> findUserRandomMatchingWithFetch(@Param("id") long id);

    @Modifying
    @Transactional
    @Query("""
            update UserRandomMatching u
            set u.matchingStatus = :matchingStatus
            where u.id = :id
            """)
    void updateMatchingStatus(@Param("id") long id, @Param("matchingStatus") RandomMatchingStatus matchingStatus);


    @Query("""
            select u
            from UserRandomMatching u
            join fetch u.randomMatching
            join fetch u.user
            where u.id = :id
            """)
    Optional<UserRandomMatching> findByIdWithRandomMatchingAndUser(@Param("id") long id);

    @Query("""
            select u
            from UserRandomMatching u
            join fetch u.user
            where u.randomMatching.id = :matchingId
            """)
    List<UserRandomMatching> findRandomMatchingParticipants(@Param("matchingId") long matchingId);

    @Query("""
            select u
            from UserRandomMatching u
            join fetch u.randomMatching
            where u.user.id = :userId
            and u.isReviewPopupDismissed = false
            """)
    List<UserRandomMatching> findUserRandomMatchingsReviewUnwritten(@Param("userId") long userId);

    @Modifying
    @Transactional
    @Query("""
            update UserRandomMatching u
            set u.isReviewPopupDismissed = true
            where u.user.id = :userId
            and u.randomMatching.id = :matchingId
            """)
    void postponeRandomMatchingReview(@Param("userId") long userId, @Param("matchingId") long matchingId);
}
