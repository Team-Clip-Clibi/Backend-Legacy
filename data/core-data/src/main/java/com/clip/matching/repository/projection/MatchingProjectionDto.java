package com.clip.matching.repository.projection;

import com.clip.matching.entity.MatchingStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MatchingProjectionDto {
    private LocalDateTime meetingTime;
    private MatchingStatus matchingStatus;
    private String matchingType;
    private Long matchingId;
    private String myOneThingContent;

    public MatchingProjectionDto(LocalDateTime meetingTime, MatchingStatus matchingStatus,
                                 String matchingType, Long matchingId, String myOneThingContent) {
        this.meetingTime = meetingTime;
        this.matchingStatus = matchingStatus;
        this.matchingType = matchingType;
        this.matchingId = matchingId;
        this.myOneThingContent = myOneThingContent;
    }

}
