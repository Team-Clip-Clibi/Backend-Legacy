package com.clip.matching.repository;

import com.clip.matching.entity.UserRandomMatching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

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
}
