package com.clip.api.matching.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RandomMatchingSummaryDto {
    private long matchingId;
    private long daysUntilMeeting;
    private String meetingPlace;

    @Builder
    public RandomMatchingSummaryDto(long matchingId, long daysUntilMeeting, String meetingPlace) {
        this.matchingId = matchingId;
        this.daysUntilMeeting = daysUntilMeeting;
        this.meetingPlace = meetingPlace;
    }
}
