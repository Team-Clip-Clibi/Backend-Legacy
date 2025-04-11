package com.clip.office.matching.service;

import com.clip.matching.entity.RandomMatching;
import com.clip.matching.service.MatchingService;
import com.clip.matching.service.RandomMatchingDataService;
import com.clip.office.matching.controller.dto.CreateRandomMatchingDto;
import com.clip.office.matching.controller.dto.UpdateRandomMatchingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RandomMatchingService {

    private final RandomMatchingDataService randomMatchingDataService;

    private final MatchingService matchingService;

    @Transactional
    public CreateRandomMatchingDto createRandomMatching(CreateRandomMatchingDto createRandomMatchingDto) {

        RandomMatching randomMatching = RandomMatching.builder()
                .randomDistrict(createRandomMatchingDto.getRandomDistrict())
                .location(createRandomMatchingDto.getLocation())
                .restaurantName(createRandomMatchingDto.getRestaurantName())
                .meetingTime(createRandomMatchingDto.getMeetingTime())
                .build();

        randomMatchingDataService.save(randomMatching);

        return CreateRandomMatchingDto.builder()
                .randomDistrict(randomMatching.getRandomDistrict())
                .location(createRandomMatchingDto.getLocation())
                .restaurantName(createRandomMatchingDto.getRestaurantName())
                .meetingTime(createRandomMatchingDto.getMeetingTime())
                .build();
    }

    @Transactional
    public UpdateRandomMatchingDto updateRandomMatching(Long randomMatchingId,UpdateRandomMatchingDto updateRandomMatchingDto) {
        RandomMatching randomMatching = matchingService.findRandomMatching(randomMatchingId);

        randomMatching.update(
                updateRandomMatchingDto.getRandomDistrict(),
                updateRandomMatchingDto.getLocation(),
                updateRandomMatchingDto.getRestaurantName(),
                updateRandomMatchingDto.getMeetingTime());

        randomMatchingDataService.save(randomMatching);

        return UpdateRandomMatchingDto.builder()
                .randomDistrict(randomMatching.getRandomDistrict())
                .location(updateRandomMatchingDto.getLocation())
                .restaurantName(updateRandomMatchingDto.getRestaurantName())
                .meetingTime(updateRandomMatchingDto.getMeetingTime())
                .build();
    }

    public void deleteRandomMatching(Long randomMatchingId) {
        randomMatchingDataService.delete(randomMatchingId);
    }
}