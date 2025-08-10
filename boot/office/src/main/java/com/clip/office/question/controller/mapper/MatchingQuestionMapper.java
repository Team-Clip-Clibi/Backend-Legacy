package com.clip.office.question.controller.mapper;

import com.clip.matching.entity.OneThingMatching;
import com.clip.matching.entity.RandomMatching;
import com.clip.office.matching.controller.dto.MatchingType;
import com.clip.office.question.controller.dto.MatchingQuestionInfoDto;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Component
public class MatchingQuestionMapper {
    public Slice<MatchingQuestionInfoDto> onethingToMatchingQuestionInfoDto(Slice<OneThingMatching> oneThingMatchings) {
        return oneThingMatchings.map(
                o -> new MatchingQuestionInfoDto(
                        o.getId(),
                        o.getRestaurantName(),
                        o.getAddress(),
                        o.getDateTime(),
                        MatchingType.ONETHING)
        );
    }

    public Slice<MatchingQuestionInfoDto> randomToMatchingQuestionInfoDto(Slice<RandomMatching> randomMatchings) {
        return randomMatchings.map(
                r -> new MatchingQuestionInfoDto(
                        r.getId(),
                        r.getRestaurantName(),
                        r.getAddress(),
                        r.getDateTime(),
                        MatchingType.RANDOM)
        );
    }
}
