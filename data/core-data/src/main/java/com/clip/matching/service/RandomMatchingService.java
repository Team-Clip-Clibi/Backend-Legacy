package com.clip.matching.service;

import com.clip.matching.entity.RandomDistrict;
import com.clip.matching.entity.RandomMatching;
import com.clip.matching.repository.RandomMatchingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RandomMatchingService {
    private final RandomMatchingRepository randomMatchingRepository;

    public RandomMatching save(RandomMatching randomMatching) {
        return randomMatchingRepository.save(randomMatching);
    }

    public Slice<RandomMatching> findMatchingList(
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            RandomDistrict district,
            int page
    ) {
        return randomMatchingRepository.findMatchingList(
                startDateTime,
                endDateTime,
                district,
                PageRequest.of(page, 30)
        );
    }

    public List<RandomMatching> findByIds(List<Long> ids) {
        return randomMatchingRepository.findAllByIds(ids);
    }

    public List<RandomMatching> saveAll(List<RandomMatching> randomMatchings) {
        return randomMatchingRepository.saveAll(randomMatchings);
    }
}
