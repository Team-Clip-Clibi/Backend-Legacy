package com.clip.api.matching.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class RandomMatchingDuplicateCheckDto {
    private LocalDateTime meetingTime;
    private Boolean isDuplicated;

    @Builder
    public RandomMatchingDuplicateCheckDto(LocalDateTime meetingTime, Boolean isDuplicated) {
        this.meetingTime = meetingTime;
        this.isDuplicated = isDuplicated;
    }
}
