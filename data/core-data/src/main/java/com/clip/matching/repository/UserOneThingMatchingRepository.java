package com.clip.matching.repository;

import com.clip.matching.entity.UserOneThingMatching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

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
}
