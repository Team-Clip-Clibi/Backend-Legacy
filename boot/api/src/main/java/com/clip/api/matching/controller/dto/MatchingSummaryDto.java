package com.clip.api.matching.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class MatchingSummaryDto {

    private List<OnethingMatchingSummaryDto> oneThingMatchings;
    private List<RandomMatchingSummaryDto> randomMatchings;

    @Builder
    public MatchingSummaryDto(List<OnethingMatchingSummaryDto> oneThingMatchings,
                              List<RandomMatchingSummaryDto> randomMatchings) {
        this.oneThingMatchings = oneThingMatchings;
        this.randomMatchings = randomMatchings;
    }

}
