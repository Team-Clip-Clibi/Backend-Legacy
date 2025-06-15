package com.clip.matching.repository.projection;

import com.clip.matching.entity.MatchingStatus;
import com.clip.matching.entity.RandomMatchingStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MatchingProjectionDto {
    private LocalDateTime meetingTime;
    private MatchingStatus matchingStatus;
    private String matchingType;
    private Long matchingId;
    private String myOneThingContent;
    private Boolean isReviewWritten;

    public MatchingProjectionDto(LocalDateTime meetingTime, MatchingStatus matchingStatus,
                                 String matchingType, Long matchingId, String myOneThingContent,
                                 Boolean isReviewWritten) {
        this.meetingTime = meetingTime;
        this.matchingStatus = matchingStatus;
        this.matchingType = matchingType;
        this.matchingId = matchingId;
        this.myOneThingContent = myOneThingContent;
        this.isReviewWritten = isReviewWritten;
    }

}
