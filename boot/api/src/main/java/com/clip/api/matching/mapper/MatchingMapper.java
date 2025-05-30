package com.clip.api.matching.mapper;

import com.clip.api.matching.controller.dto.MatchingDto;
import com.clip.api.matching.controller.dto.OnethingMatchingSummaryDto;
import com.clip.api.matching.controller.dto.RandomMatchingSummaryDto;
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

    List<MatchingDto> toDto(List<MatchingProjectionDto> matchingProjectionDto);
}
