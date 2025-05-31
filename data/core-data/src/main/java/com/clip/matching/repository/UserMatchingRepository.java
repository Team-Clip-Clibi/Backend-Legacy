package com.clip.matching.repository;

import com.clip.matching.entity.MatchingStatus;
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
            otm.meetingTime,
            uotm.matchingStatus,
            'ONE_THING',
            otm.id,
            uotm.myOneThingContent,
            case when uotm.matchingStatus = 'COMPLETED' and otr.id is not null then true else false end
        )
        from UserOneThingMatching uotm
        join uotm.oneThingMatching otm
        left join OneThingMatchingReview otr on otr.oneThingMatching.id = otm.id and otr.user.id = :userId
        where (:matchingStatus is null or uotm.matchingStatus = :matchingStatus)
        and (:lastMeetingTime is null or uotm.oneThingMatching.meetingTime < :lastMeetingTime)
        and uotm.user.id = :userId
        order by uotm.oneThingMatching.meetingTime desc
        
        union all
        
        select new com.clip.matching.repository.projection.MatchingProjectionDto(
            rm.meetingTime,
            urm.matchingStatus,
            'RANDOM',
            rm.id,
            urm.myOneThingContent,
            case when urm.matchingStatus = 'COMPLETED' and rmr.id is not null then true else false end
        )
        from UserRandomMatching urm
        join urm.randomMatching rm
        left join RandomMatchingReview rmr on rmr.randomMatching.id = rm.id and rmr.user.id = :userId
        where (:matchingStatus is null or urm.matchingStatus = :matchingStatus)
        and (:lastMeetingTime is null or urm.randomMatching.meetingTime < :lastMeetingTime)
        and urm.user.id = :userId
        order by urm.randomMatching.meetingTime desc
        """)
    List<MatchingProjectionDto> findAllMatchingsByStatus(
            @Param("matchingStatus") MatchingStatus matchingStatus,
            @Param("lastMeetingTime") LocalDateTime lastMeetingTime,
            @Param("userId") long userId,
            Pageable page);
}
