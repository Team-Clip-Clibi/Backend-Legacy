package com.clip.api.matching.mapper;

import com.clip.api.matching.controller.dto.MatchingReviewDto;
import com.clip.api.matching.controller.dto.MatchingReviewPopupDto;
import com.clip.api.matching.controller.dto.MatchingType;
import com.clip.matching.entity.*;
import com.clip.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MatchingReviewMapper {
    OneThingMatchingReview toOneThingMatchingReview(final User user, final OneThingMatching oneThingMatching, final MatchingReviewDto matchingReviewDto);

    RandomMatchingReview toRandomMatchingReview(final User user, final RandomMatching randomMatching, final MatchingReviewDto matchingReviewDto);

    default List<MatchingReviewPopupDto> toMatchingReviewPopupDtoList(final List<UserOneThingMatching> oneThingMatchings, final List<UserRandomMatching> randomMatchings) {
        List<MatchingReviewPopupDto> matchingReviewPopupDtos = new ArrayList<>();
        matchingReviewPopupDtos.addAll(oneThingMatchings.stream()
                .map(userOneThingMatching -> MatchingReviewPopupDto.builder()
                        .matchingId(userOneThingMatching.getOneThingMatching().getId())
                        .meetingTime(userOneThingMatching.getOneThingMatching().getMeetingTime())
                        .matchingType(MatchingType.ONE_THING)
                        .build())
                .toList());
        matchingReviewPopupDtos.addAll(randomMatchings.stream()
                .map(userRandomMatching -> MatchingReviewPopupDto.builder()
                        .matchingId(userRandomMatching.getRandomMatching().getId())
                        .meetingTime(userRandomMatching.getRandomMatching().getMeetingTime())
                        .matchingType(MatchingType.RANDOM)
                        .build())
                .toList());
        return matchingReviewPopupDtos;
    }
}
