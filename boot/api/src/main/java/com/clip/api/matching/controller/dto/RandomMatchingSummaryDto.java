package com.clip.api.matching.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class RandomMatchingSummaryDto {
    private long matchingId;
    private long daysUntilMeeting;
    private LocalDateTime meetingTime;
    private String meetingPlace;

    @Builder
    public RandomMatchingSummaryDto(long matchingId, long daysUntilMeeting, LocalDateTime meetingTime, String meetingPlace) {
        this.matchingId = matchingId;
        this.daysUntilMeeting = daysUntilMeeting;
        this.meetingTime = meetingTime;
        this.meetingPlace = meetingPlace;
    }
}
