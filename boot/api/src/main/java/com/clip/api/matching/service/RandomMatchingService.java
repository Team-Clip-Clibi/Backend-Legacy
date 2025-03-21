package com.clip.api.matching.service;

import com.clip.api.matching.controller.dto.CreateRandomMatchingDto;
import com.clip.matching.entity.RandomMatching;
import com.clip.matching.service.RandomMatchingDataService;
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
                .city(createRandomMatchingDto.getCity())
                .location(createRandomMatchingDto.getLocation())
                .restaurantName(createRandomMatchingDto.getRestaurantName())
                .meetingTime(createRandomMatchingDto.getMeetingTime())
                .build();

        randomMatchingDataService.save(randomMatching);

        return CreateRandomMatchingDto.builder()
                .city(createRandomMatchingDto.getCity())
                .location(createRandomMatchingDto.getLocation())
                .restaurantName(createRandomMatchingDto.getRestaurantName())
                .meetingTime(createRandomMatchingDto.getMeetingTime())
                .build();
    }
}