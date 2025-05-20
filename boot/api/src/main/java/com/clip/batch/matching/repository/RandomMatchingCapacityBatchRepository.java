package com.clip.batch.matching.repository;

import com.clip.matching.repository.RandomMatchingCapacityRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface RandomMatchingCapacityBatchRepository extends RandomMatchingCapacityRepository {

    @Transactional
    @Modifying
    @Query("update RandomMatchingCapacity r set r.availableCapacity = r.availableCapacity + 1, r.pendingCapacity = r.pendingCapacity - 1")
    void updateAvailableCapacity();
}
