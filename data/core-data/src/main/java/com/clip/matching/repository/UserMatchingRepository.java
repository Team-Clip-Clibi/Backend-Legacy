package com.clip.matching.repository;

import com.clip.matching.entity.OneThingMatchingStatus;
import com.clip.matching.entity.RandomMatchingStatus;
import com.clip.matching.entity.UserOneThingMatching;
import com.clip.matching.repository.projection.MatchingProjectionDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface UserMatchingRepository extends JpaRepository<UserOneThingMatching, Long> {

    @Query("""
        select new com.clip.matching.repository.projection.MatchingProjectionDto(
            uotm.id,
            otm.meetingTime,
            uotm.matchingStatus,
            'ONE_THING',
            uotm.myOneThingContent,
            case when uotm.matchingStatus = 'COMPLETED' and otr.id is not null then true else false end
        )
        from UserOneThingMatching uotm
        left join uotm.oneThingMatching otm
        left join OneThingMatchingReview otr on otr.oneThingMatching.id = otm.id and otr.user.id = :userId
        where (:matchingStatus is null or uotm.matchingStatus = :matchingStatus)
        and (:lastMeetingTime is null or uotm.oneThingMatching.meetingTime < :lastMeetingTime)
        and uotm.user.id = :userId
        and uotm.matchingStatus != 'WAIT_FOR_PAYMENT'
        order by uotm.oneThingMatching.meetingTime desc
        
        union all
        
        select new com.clip.matching.repository.projection.MatchingProjectionDto(
            urm.id,
            rm.meetingTime,
            urm.matchingStatus,
            'RANDOM',
            urm.myOneThingContent,
            case when urm.matchingStatus = 'COMPLETED' and rmr.id is not null then true else false end
        )
        from UserRandomMatching urm
        join urm.randomMatching rm
        left join RandomMatchingReview rmr on rmr.randomMatching.id = rm.id and rmr.user.id = :userId
        where urm.user.id = :userId
          and urm.matchingStatus != 'APPLIED'
          and (:matchingStatus is null or urm.matchingStatus = :matchingStatus)
          and (:lastMeetingTime is null or rm.meetingTime < :lastMeetingTime)
        order by urm.randomMatching.meetingTime desc
        """)
    List<MatchingProjectionDto> findAllMatchingsByStatus(
            @Param("matchingStatus") RandomMatchingStatus matchingStatus,
            @Param("lastMeetingTime") LocalDateTime lastMeetingTime,
            @Param("userId") long userId,
            Pageable page);
}
