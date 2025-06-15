package com.clip.matching.repository.projection;

import com.clip.matching.entity.MatchingStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MatchingProjectionDto {
    private Long id;
    private LocalDateTime meetingTime;
    private MatchingStatus matchingStatus;
    private String matchingType;
    private String myOneThingContent;
    private Boolean isReviewWritten;

    public MatchingProjectionDto(Long id, LocalDateTime meetingTime, MatchingStatus matchingStatus,
                                 String matchingType, String myOneThingContent,
                                 Boolean isReviewWritten) {
        this.id = id;
        this.meetingTime = meetingTime;
        this.matchingStatus = matchingStatus;
        this.matchingType = matchingType;
        this.myOneThingContent = myOneThingContent;
        this.isReviewWritten = isReviewWritten;
    }

}
