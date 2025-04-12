package com.clip.api.matching.mapper;

import com.clip.api.matching.controller.dto.OnethingMatchingSummaryDto;
import com.clip.api.matching.controller.dto.RandomMatchingSummaryDto;
import com.clip.matching.entity.UserOneThingMatching;
import com.clip.matching.entity.UserRandomMatching;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDate;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MatchingMapper {


    default OnethingMatchingSummaryDto toDto(final UserOneThingMatching userOneThingMatching){
        return OnethingMatchingSummaryDto.builder()
                .matchingId(userOneThingMatching.getOneThingMatching().getId())
                .daysUntilMeeting(userOneThingMatching.getOneThingMatching().getMeetingTime().toLocalDate().toEpochDay() - LocalDate.now().toEpochDay())
                .meetingPlace(userOneThingMatching.getOneThingMatching().getLocation())
                .build();
    }

    default RandomMatchingSummaryDto toDto(final UserRandomMatching userRandomMatching){
        return RandomMatchingSummaryDto.builder()
                .matchingId(userRandomMatching.getRandomMatching().getId())
                .daysUntilMeeting(userRandomMatching.getRandomMatching().getMeetingTime().toLocalDate().toEpochDay() - LocalDate.now().toEpochDay())
                .meetingPlace(userRandomMatching.getRandomMatching().getLocation())
                .build();
    }
}
