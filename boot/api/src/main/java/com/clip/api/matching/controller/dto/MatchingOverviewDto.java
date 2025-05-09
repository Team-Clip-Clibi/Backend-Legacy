package com.clip.api.matching.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class MatchingOverviewDto {
    LocalDate nextMatchingDate;
    long appliedMatchingCount;
    long confirmedMatchingCount;
    Boolean isAllNoticeRead;

    @Builder
    public MatchingOverviewDto(LocalDate nextMatchingDate, long appliedMatchingCount, long confirmedMatchingCount, Boolean isAllNoticeRead) {
        this.nextMatchingDate = nextMatchingDate;
        this.appliedMatchingCount = appliedMatchingCount;
        this.confirmedMatchingCount = confirmedMatchingCount;
        this.isAllNoticeRead = isAllNoticeRead;
    }
}
