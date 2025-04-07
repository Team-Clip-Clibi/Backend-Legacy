package com.clip.office.matching.service;

import com.clip.matching.entity.RandomMatching;
import com.clip.matching.service.RandomMatchingDataService;
import com.clip.office.matching.controller.dto.CreateRandomMatchingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RandomMatchingService {

    private final RandomMatchingDataService randomMatchingDataService;

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
}