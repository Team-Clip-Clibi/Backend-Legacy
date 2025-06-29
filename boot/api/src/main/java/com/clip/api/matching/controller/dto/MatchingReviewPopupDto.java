package com.clip.api.matching.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class MatchingReviewPopupDto {
    private Long matchingId;
    private LocalDateTime meetingTime;
    private MatchingType matchingType;

    @Builder
    public MatchingReviewPopupDto(Long matchingId, LocalDateTime meetingTime, MatchingType matchingType) {
        this.matchingId = matchingId;
        this.meetingTime = meetingTime;
        this.matchingType = matchingType;
    }

}
