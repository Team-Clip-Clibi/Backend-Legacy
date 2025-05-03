package com.clip.office.matching.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clip.matching.entity.OneThingMatching;
import com.clip.matching.service.MatchingService;
import com.clip.matching.service.OneThingMatchingDataService;
import com.clip.office.matching.controller.dto.CreateOneThingMatchingDto;
import com.clip.office.matching.controller.dto.UpdateOneThingMatchingDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OneThingMatchingService {

    private final OneThingMatchingDataService oneThingMatchingDataService;
    private final MatchingService matchingService;

    @Transactional(readOnly = true)
    public List<OneThingMatching> findAllOneThingMatchings() {
        return oneThingMatchingDataService.findAllOneThingMatchings();
    }

    @Transactional
    public void createOneThingMatching(CreateOneThingMatchingDto createOneThingMatchingDto) {

        OneThingMatching oneThingMatching = OneThingMatching.builder()
                .oneThingDistrict(createOneThingMatchingDto.getOneThingDistrict())
                .location(createOneThingMatchingDto.getLocation())
                .restaurantName(createOneThingMatchingDto.getRestaurantName())
                .meetingTime(createOneThingMatchingDto.getMeetingTime())
                .oneThingPrice(createOneThingMatchingDto.getOneThingPrice())
                .build();

        oneThingMatchingDataService.save(oneThingMatching);
    }

    @Transactional
    public void updateOneThingMatching(Long oneThingMatchingId, UpdateOneThingMatchingDto updateOneThingMatchingDto) {
        OneThingMatching oneThingMatching = matchingService.findOneThingMatching(oneThingMatchingId);

        oneThingMatching.update(
                updateOneThingMatchingDto.getOneThingDistrict(),
                updateOneThingMatchingDto.getLocation(),
                updateOneThingMatchingDto.getRestaurantName(),
                updateOneThingMatchingDto.getMeetingTime(),
                updateOneThingMatchingDto.getOneThingBudgetRange());

        oneThingMatchingDataService.save(oneThingMatching);
    }

    @Transactional(readOnly = true)
    public UpdateOneThingMatchingDto getUpdateOneThingMatchingDto(Long oneThingMatchingId) {
        OneThingMatching oneThingMatching = matchingService.findOneThingMatching(oneThingMatchingId);

        return UpdateOneThingMatchingDto.builder()
                .oneThingDistrict(oneThingMatching.getOneThingDistrict())
                .location(oneThingMatching.getLocation())
                .restaurantName(oneThingMatching.getRestaurantName())
                .meetingTime(oneThingMatching.getMeetingTime())
                .oneThingBudgetRange(oneThingMatching.getOneThingPrice())
                .build();
    }

    public void deleteOneThingMatching(Long oneThingMatchingId) {
        oneThingMatchingDataService.delete(oneThingMatchingId);
    }
}
