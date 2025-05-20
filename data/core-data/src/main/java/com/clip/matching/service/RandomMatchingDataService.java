package com.clip.matching.service;

import com.clip.matching.entity.RandomMatching;
import com.clip.matching.entity.RandomMatchingCapacity;
import com.clip.matching.repository.RandomMatchingCapacityRepository;
import com.clip.matching.repository.RandomMatchingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RandomMatchingDataService {

    private final RandomMatchingRepository randomMatchingRepository;
    private final RandomMatchingCapacityRepository randomMatchingCapacityRepository;

    public RandomMatching save(RandomMatching randomMatching) {
        return randomMatchingRepository.save(randomMatching);
    }

    public List<RandomMatching> findAllRandomMatchings() {return randomMatchingRepository.findAll();}

    public void delete(Long randomMatchingId) {
        randomMatchingRepository.deleteRandomMatching(randomMatchingId);
    }

    public RandomMatchingCapacity save(RandomMatchingCapacity randomMatchingCapacity) {
        return randomMatchingCapacityRepository.save(randomMatchingCapacity);
    }
}
