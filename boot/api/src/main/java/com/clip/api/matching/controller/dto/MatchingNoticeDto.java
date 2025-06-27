package com.clip.api.matching.controller.dto;

import com.clip.matching.entity.MatchingStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class MatchingNoticeDto {
    private long matchingId;
    private LocalDateTime meetingTime;
    private MatchingStatus matchingStatus;
    private MatchingType matchingType;
    private String myOneThingContent;
    private String restaurantName;
    private String location;
    private String menuCategory;
    private List<JobInfo> jobInfos;
    private List<DietaryInfo> dietaryInfos;

    @Getter
    @Builder
    public static class JobInfo {
        private String jobName;
        private int count;

        public JobInfo(String jobName, int count) {
            this.jobName = jobName;
            this.count = count;
        }
    }

    @Getter
    @Builder
    public static class DietaryInfo {
        private String dietaryOption;
        private int count;

        public DietaryInfo(String dietaryOption, int count) {
            this.dietaryOption = dietaryOption;
            this.count = count;
        }
    }
}
