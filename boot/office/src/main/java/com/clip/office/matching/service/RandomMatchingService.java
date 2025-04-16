package com.clip.office.matching.service;

import com.clip.matching.entity.RandomMatching;
import com.clip.matching.service.MatchingService;
import com.clip.matching.service.RandomMatchingDataService;
import com.clip.office.matching.controller.dto.CreateRandomMatchingDto;
import com.clip.office.matching.controller.dto.UpdateRandomMatchingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RandomMatchingService {

    private final RandomMatchingDataService randomMatchingDataService;

    private final MatchingService matchingService;

    @Transactional(readOnly = true)
    public List<RandomMatching> findAllRandomMatchings() {
        return randomMatchingDataService.findAllRandomMatchings();
    }

    @Transactional
    public void createRandomMatching(CreateRandomMatchingDto createRandomMatchingDto) {

        RandomMatching randomMatching = RandomMatching.builder()
                .randomDistrict(createRandomMatchingDto.getRandomDistrict())
                .location(createRandomMatchingDto.getLocation())
                .restaurantName(createRandomMatchingDto.getRestaurantName())
                .meetingTime(createRandomMatchingDto.getMeetingTime())
                .build();

        randomMatchingDataService.save(randomMatching);
    }

    @Transactional
    public void updateRandomMatching(Long randomMatchingId, UpdateRandomMatchingDto updateRandomMatchingDto) {
        RandomMatching randomMatching = matchingService.findRandomMatching(randomMatchingId);

        randomMatching.update(
                updateRandomMatchingDto.getRandomDistrict(),
                updateRandomMatchingDto.getLocation(),
                updateRandomMatchingDto.getRestaurantName(),
                updateRandomMatchingDto.getMeetingTime());

        randomMatchingDataService.save(randomMatching);
    }

    @Transactional(readOnly = true)
    public UpdateRandomMatchingDto getUpdateRandomMatchingDto(Long randomMatchingId) {
        RandomMatching randomMatching = matchingService.findRandomMatching(randomMatchingId);

        return UpdateRandomMatchingDto.builder()
                .randomDistrict(randomMatching.getRandomDistrict())
                .location(randomMatching.getLocation())
                .restaurantName(randomMatching.getRestaurantName())
                .meetingTime(randomMatching.getMeetingTime())
                .build();
    }

    public void deleteRandomMatching(Long randomMatchingId) {
        randomMatchingDataService.delete(randomMatchingId);
    }
}