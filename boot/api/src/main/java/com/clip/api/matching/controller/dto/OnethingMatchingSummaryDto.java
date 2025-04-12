package com.clip.api.matching.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OnethingMatchingSummaryDto {
    private long matchingId;
    private long daysUntilMeeting;
    private String meetingPlace;

    @Builder
    public OnethingMatchingSummaryDto(long matchingId, long daysUntilMeeting, String meetingPlace) {
        this.matchingId = matchingId;
        this.daysUntilMeeting = daysUntilMeeting;
        this.meetingPlace = meetingPlace;
    }

}
