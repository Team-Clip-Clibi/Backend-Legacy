package com.clip.matching.repository;

import com.clip.matching.entity.RandomDistrict;
import com.clip.matching.entity.RandomMatchingCapacity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RandomMatchingCapacityRepository extends JpaRepository<RandomMatchingCapacity, Long> {

    @Query("""
            select rmc from RandomMatchingCapacity rmc
            join fetch rmc.randomMatching rm
            where rm.randomDistrict = :district
            and rm.meetingTime >= :matchingTime
            and rm.meetingTime < :matchingTimeEnd
            order by rmc.availableCapacity desc
        """)
    List<RandomMatchingCapacity> findRandomMatchingCapacitiesWithDistrict(@Param("district") RandomDistrict district, @Param("matchingTime") LocalDateTime matchingTime,
                                                                           @Param("matchingTimeEnd") LocalDateTime matchingTimeEnd);

    @Query("""
            select rmc from RandomMatchingCapacity rmc
            join fetch rmc.randomMatching rm
            where rm.id = :randomMatchingId
        """)
    Optional<RandomMatchingCapacity> findRandomMatchingCapacity(@Param("randomMatchingId") Long randomMatchingId);
}
