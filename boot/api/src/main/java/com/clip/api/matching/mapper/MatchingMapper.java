package com.clip.api.matching.mapper;

import com.clip.api.matching.controller.dto.*;
import com.clip.matching.entity.UserOneThingMatching;
import com.clip.matching.entity.UserRandomMatching;
import com.clip.matching.repository.projection.MatchingProjectionDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDate;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MatchingMapper {


    default OnethingMatchingSummaryDto toDto(final UserOneThingMatching userOneThingMatching){
        return OnethingMatchingSummaryDto.builder()
                .matchingId(userOneThingMatching.getOneThingMatching().getId())
                .daysUntilMeeting(userOneThingMatching.getOneThingMatching().getMeetingTime().toLocalDate().toEpochDay() - LocalDate.now().toEpochDay())
                .meetingTime(userOneThingMatching.getOneThingMatching().getMeetingTime())
                .meetingPlace(userOneThingMatching.getOneThingMatching().getLocation())
                .build();
    }

    default RandomMatchingSummaryDto toDto(final UserRandomMatching userRandomMatching){
        return RandomMatchingSummaryDto.builder()
                .matchingId(userRandomMatching.getRandomMatching().getId())
                .daysUntilMeeting(userRandomMatching.getRandomMatching().getMeetingTime().toLocalDate().toEpochDay() - LocalDate.now().toEpochDay())
                .meetingTime(userRandomMatching.getRandomMatching().getMeetingTime())
                .meetingPlace(userRandomMatching.getRandomMatching().getLocation())
                .build();
    }

    default OneThingMatchingDetailDto toOneThingMatchingDetailDto(final UserOneThingMatching userOneThingMatching) {
        return OneThingMatchingDetailDto.builder()
                .matchingId(userOneThingMatching.getOneThingMatching().getId())
                .meetingTime(userOneThingMatching.getOneThingMatching().getMeetingTime())
                .matchingStatus(userOneThingMatching.getMatchingStatus())
                .matchingType(MatchingType.ONE_THING)
                .myOneThingContent(userOneThingMatching.getMyOneThingContent())
                .applicationInfo(OneThingMatchingDetailDto.ApplicationInfo.builder()
                    .district(userOneThingMatching.getOneThingDistrict().name())
                    .preferredDates(userOneThingMatching.getPreferredDates())
                    .oneThingBudgetRange(userOneThingMatching.getOneThingBudgetRange())
                    .oneThingCategory(userOneThingMatching.getOneThingCategory())
                    .build())
                .myMatchingInfo(MatchingDetailDto.MyMatchingInfo.builder()
                        .job(userOneThingMatching.getUser().getJob().getJobCategory())
                        .relationshipStatus(userOneThingMatching.getUser().getRelationshipStatus())
                        .dietaryOption(userOneThingMatching.getUser().getDietaryOption())
                        .language(userOneThingMatching.getUser().getLanguage())
                        .build())
                .paymentInfo(OneThingMatchingDetailDto.PaymentInfo.builder()
                        .matchingPrice(userOneThingMatching.getOneThingOrder().getPrice().getBasePrice().intValue())
                        .paymentPrice(userOneThingMatching.getOneThingOrder().getDiscountedPrice().intValue())
                        .build())
                .build();
    }

    default RandomMatchingDetailDto toRandomMatchingDetailDto(final UserRandomMatching userRandomMatching) {
        return RandomMatchingDetailDto.builder()
                .matchingId(userRandomMatching.getRandomMatching().getId())
                .meetingTime(userRandomMatching.getRandomMatching().getMeetingTime())
                .matchingStatus(userRandomMatching.getMatchingStatus())
                .matchingType(MatchingType.RANDOM)
                .myOneThingContent(userRandomMatching.getMyOneThingContent())
                .applicationInfo(RandomMatchingDetailDto.ApplicationInfo.builder()
                    .district(userRandomMatching.getRandomMatching().getRandomDistrict().name())
                    .build())
                .myMatchingInfo(MatchingDetailDto.MyMatchingInfo.builder()
                        .job(userRandomMatching.getUser().getJob().getJobCategory())
                        .relationshipStatus(userRandomMatching.getUser().getRelationshipStatus())
                        .dietaryOption(userRandomMatching.getUser().getDietaryOption())
                        .language(userRandomMatching.getUser().getLanguage())
                        .build())
                .paymentInfo(RandomMatchingDetailDto.PaymentInfo.builder()
                        .matchingPrice(userRandomMatching.getRandomOrder().getPrice().getBasePrice().intValue())
                        .paymentPrice(userRandomMatching.getRandomOrder().getDiscountedPrice().intValue())
                        .build())
                .build();
    }

    List<MatchingDto> toDto(List<MatchingProjectionDto> matchingProjectionDto);
}
