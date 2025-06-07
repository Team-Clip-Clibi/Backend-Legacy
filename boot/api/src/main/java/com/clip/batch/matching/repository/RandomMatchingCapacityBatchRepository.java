package com.clip.batch.matching.repository;

import com.clip.matching.repository.RandomMatchingCapacityRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RandomMatchingCapacityBatchRepository extends RandomMatchingCapacityRepository {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            update RandomMatchingCapacity c
            set c.availableCapacity = c.availableCapacity + 1,
            c.pendingCapacity = c.pendingCapacity - 1
            where c.randomMatching.id in :matchingIds
            """)
    void updateAvailableCapacity(List<Long> matchingIds);
}
