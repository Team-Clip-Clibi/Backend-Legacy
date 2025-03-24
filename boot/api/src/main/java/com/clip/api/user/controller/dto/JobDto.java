package com.clip.api.user.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class JobDto {
    private List<JobCategory> jobList;

    @Builder
    public JobDto(List<JobCategory> jobList) {
        this.jobList = jobList;
    }
}
