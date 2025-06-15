package com.clip.api.matching.controller.dto;

import com.clip.matching.entity.MatchingStatus;
import com.clip.matching.entity.RandomMatchingStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class MatchingDto {
    private Long id;
    private LocalDateTime meetingTime;
    private MatchingStatus matchingStatus;
    private String matchingType;
    private String myOneThingContent;
    private Boolean isReviewWritten;

    @Builder
    public MatchingDto(Long id, LocalDateTime meetingTime, MatchingStatus matchingStatus,
                       String matchingType, String myOneThingContent, Boolean isReviewWritten) {
        this.id = id;
        this.meetingTime = meetingTime;
        this.matchingStatus = matchingStatus;
        this.matchingType = matchingType;
        this.myOneThingContent = myOneThingContent;
        this.isReviewWritten = isReviewWritten;
    }
}
