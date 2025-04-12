package com.clip.api.matching.service;

import com.clip.api.matching.controller.dto.MatchingSummaryDto;
import com.clip.api.matching.mapper.MatchingMapper;
import com.clip.matching.entity.UserOneThingMatching;
import com.clip.matching.entity.UserRandomMatching;
import com.clip.matching.service.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserMatchingService {

    private final MatchingService matchingService;
    private final MatchingMapper matchingMapper;

    public MatchingSummaryDto getUserMatchings(final long userId) {
        List<UserOneThingMatching> userOneThingMatchings = matchingService.findUserOneThingMatchings(userId);
        List<UserRandomMatching> userRandomMatchings = matchingService.findUserRandomMatchings(userId);
        return MatchingSummaryDto.builder()
                .oneThingMatchings(userOneThingMatchings.stream()
                        .map(matchingMapper::toDto)
                        .toList())
                .randomMatchings(userRandomMatchings.stream()
                        .map(matchingMapper::toDto)
                        .toList())
                .build();
    }


}
