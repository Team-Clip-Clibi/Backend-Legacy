package com.clip.api.user.controller.dto;

import com.clip.user.entity.JobCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JobDto {
    private JobCategory job;

    @Builder
    public JobDto(JobCategory job) {
        this.job = job;
    }
}
