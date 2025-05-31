package com.clip.api.matching.controller.dto;

import com.clip.matching.entity.MatchingStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class MatchingDto {
    private LocalDateTime meetingTime;
    private MatchingStatus matchingStatus;
    private String matchingType;
    private Long matchingId;
    private String myOneThingContent;
    private Boolean isReviewWritten;

    @Builder
    public MatchingDto(LocalDateTime meetingTime, MatchingStatus matchingStatus,
                       String matchingType, Long matchingId, String myOneThingContent, Boolean isReviewWritten) {
        this.meetingTime = meetingTime;
        this.matchingStatus = matchingStatus;
        this.matchingType = matchingType;
        this.matchingId = matchingId;
        this.myOneThingContent = myOneThingContent;
        this.isReviewWritten = isReviewWritten;
    }
}
