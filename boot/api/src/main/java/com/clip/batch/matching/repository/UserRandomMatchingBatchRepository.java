package com.clip.batch.matching.repository;

import com.clip.matching.repository.UserRandomMatchingRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRandomMatchingBatchRepository extends UserRandomMatchingRepository {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            delete from UserRandomMatching urm
            where urm.randomMatching.id in :matchingIds
            """)
    void deleteByMatchingIds(List<Long> matchingIds);
}
