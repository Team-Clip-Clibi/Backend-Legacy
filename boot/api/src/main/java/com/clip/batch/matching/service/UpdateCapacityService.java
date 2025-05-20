package com.clip.batch.matching.service;

import com.clip.batch.matching.repository.RandomMatchingCapacityBatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateCapacityService {

    private final RandomMatchingCapacityBatchRepository randomMatchingCapacityBatchRepository;

    public void updateAvailableCapacity() {
        randomMatchingCapacityBatchRepository.updateAvailableCapacity();
    }

}
