package com.clip.api.matching.mapper;

import com.clip.api.matching.controller.dto.MatchingNoticeDto;
import com.clip.api.matching.controller.dto.MatchingType;
import com.clip.matching.entity.UserOneThingMatching;
import com.clip.matching.entity.UserRandomMatching;
import com.clip.matching.repository.projection.ParticipantJobAndDietaryDto;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class MatchingNoticeMapper {
    public List<MatchingNoticeDto> toMatchingNoticeDtoList(
            List<UserOneThingMatching> userOneThingMatchings,
            List<ParticipantJobAndDietaryDto> onethingParticipantJobAndDietary,
            List<UserRandomMatching> userRandomMatchings,
            List<ParticipantJobAndDietaryDto> randomParticipantJobAndDietary
    ) {

        return Stream.concat(
                        userOneThingMatchings.stream().map(uom -> toMatchingNoticeDto(uom, onethingParticipantJobAndDietary)),
                        userRandomMatchings.stream().map(urm -> toMatchingNoticeDto(urm, randomParticipantJobAndDietary)))
                .sorted(Comparator.comparing(MatchingNoticeDto::getMeetingTime))
                .toList();
    }

    private MatchingNoticeDto toMatchingNoticeDto(
            UserRandomMatching userRandomMatching,
            List<ParticipantJobAndDietaryDto> randomParticipantJobAndDietary
    ) {
        List<String> dietaryList = randomParticipantJobAndDietary.stream()
                .map(ParticipantJobAndDietaryDto::dietaryOption)
                .filter(s -> !s.equals("다 잘먹어요"))
                .toList();

        List<MatchingNoticeDto.JobInfo> jobInfoList = randomParticipantJobAndDietary.stream()
                .filter(r -> r.matchingId() == userRandomMatching.getId())
                .collect(Collectors.groupingBy(
                        ParticipantJobAndDietaryDto::jobCategory,
                        Collectors.counting()))
                .entrySet().stream()
                .map(entry -> new MatchingNoticeDto.JobInfo(
                        entry.getKey().name(),
                        entry.getValue().intValue()))
                .toList();

        return MatchingNoticeDto.builder()
                .matchingId(userRandomMatching.getId())
                .meetingTime(userRandomMatching.getRandomMatching().getDateTime())
                .matchingStatus(userRandomMatching.getMatchingStatus())
                .matchingType(MatchingType.RANDOM)
                .myOneThingContent(userRandomMatching.getOnethingTopic())
                .restaurantName(userRandomMatching.getRandomMatching().getRestaurantName())
                .location(userRandomMatching.getRandomMatching().getAddress())
                .jobInfos(jobInfoList)
                .dietaryList(dietaryList)
                .build();
    }

    private MatchingNoticeDto toMatchingNoticeDto(
            UserOneThingMatching userOneThingMatching,
            List<ParticipantJobAndDietaryDto> onethingParticipantJobAndDietary
    ) {
        List<String> dietaryList = onethingParticipantJobAndDietary.stream()
                .map(ParticipantJobAndDietaryDto::dietaryOption)
                .filter(s -> !s.equals("다 잘먹어요"))
                .toList();

        List<MatchingNoticeDto.JobInfo> jobInfoList = onethingParticipantJobAndDietary.stream()
                .filter(r -> r.matchingId() == userOneThingMatching.getId())
                .collect(Collectors.groupingBy(
                        ParticipantJobAndDietaryDto::jobCategory,
                        Collectors.counting()))
                .entrySet().stream()
                .map(entry -> new MatchingNoticeDto.JobInfo(
                        entry.getKey().name(),
                        entry.getValue().intValue()))
                .toList();

        return MatchingNoticeDto.builder()
                .matchingId(userOneThingMatching.getId())
                .meetingTime(userOneThingMatching.getOneThingMatching().getDateTime())
                .matchingStatus(userOneThingMatching.getMatchingStatus())
                .matchingType(MatchingType.ONE_THING)
                .myOneThingContent(userOneThingMatching.getOnethingTopic())
                .restaurantName(userOneThingMatching.getOneThingMatching().getRestaurantName())
                .location(userOneThingMatching.getOneThingMatching().getAddress())
                .jobInfos(jobInfoList)
                .dietaryList(dietaryList)
                .build();
    }
}
