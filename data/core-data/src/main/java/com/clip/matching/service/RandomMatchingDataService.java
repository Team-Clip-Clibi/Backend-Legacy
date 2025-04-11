package com.clip.matching.service;

import com.clip.matching.entity.RandomMatching;
import com.clip.matching.repository.RandomMatchingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RandomMatchingDataService {

    private final RandomMatchingRepository randomMatchingRepository;

    public RandomMatching save(RandomMatching randomMatching) {
        return randomMatchingRepository.save(randomMatching);
    }

    public void delete(Long randomMatchingId) {
        randomMatchingRepository.deleteRandomMatching(randomMatchingId);
    }
}
