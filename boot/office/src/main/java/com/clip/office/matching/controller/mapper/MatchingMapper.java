package com.clip.office.matching.controller.mapper;

import com.clip.matching.entity.OneThingMatching;
import com.clip.matching.entity.OnethingKeyword;
import com.clip.matching.entity.RandomMatching;
import com.clip.matching.entity.UserOneThingMatching;
import com.clip.office.matching.controller.dto.MatchingInfoDto;
import com.clip.office.matching.controller.dto.MatchingType;
import com.clip.office.matching.controller.dto.ParticipantInfoDto;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Component
public class MatchingMapper {
    public Slice<MatchingInfoDto> toMatchingInfos(Slice<OneThingMatching> oneThingMatchings,
                                                  Map<Long, Long> idToParticipantCnt,
                                                  Map<Long, OnethingKeyword> idToKeywords) {
        return oneThingMatchings.map(
                oneThingMatching -> new MatchingInfoDto(
                        oneThingMatching.getId(),
                        oneThingMatching.getRestaurantName(),
                        oneThingMatching.getAddress(),
                        oneThingMatching.getDateTime(),
                        MatchingType.ONETHING,
                        idToParticipantCnt.getOrDefault(oneThingMatching.getId(), 0L).intValue(),
                        idToKeywords.getOrDefault(oneThingMatching.getId(), null)
                )
        );
    }

    public Slice<MatchingInfoDto> toMatchingInfos(Slice<RandomMatching> randomMatchings,
                                                  Map<Long, Long> idToParticipantCnt) {
        return randomMatchings.map(
                randomMatching -> new MatchingInfoDto(
                        randomMatching.getId(),
                        randomMatching.getRestaurantName(),
                        randomMatching.getAddress(),
                        randomMatching.getDateTime(),
                        MatchingType.RANDOM,
                        idToParticipantCnt.getOrDefault(randomMatching.getId(), 0L).intValue(),
                        null
                )
        );
    }

    public Slice<ParticipantInfoDto> toParticipantInfos(Slice<UserOneThingMatching> userOneThingMatchings) {
        return userOneThingMatchings.map(userOneThingMatching -> new ParticipantInfoDto(
                userOneThingMatching.getId(),
                userOneThingMatching.getUser().getNickname(),
                userOneThingMatching.getUser().getPhoneNumber(),
                userOneThingMatching.getOneThingKeyword(),
                Objects.isNull(userOneThingMatching.getUser().getJob()) ? null : userOneThingMatching.getUser().getJob().getJobCategory(),
                userOneThingMatching.getUser().getLanguage(),
                userOneThingMatching.getPreferredDates(),
                userOneThingMatching.getOnethingTopic(),
                Objects.isNull(userOneThingMatching.getOneThingMatching()) ? null : userOneThingMatching.getOneThingMatching().getRestaurantName()
        ));
    }
}
