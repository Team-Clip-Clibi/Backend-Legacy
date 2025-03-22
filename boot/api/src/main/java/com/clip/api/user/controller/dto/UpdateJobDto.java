package com.clip.api.user.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class UpdateJobDto {
    private List<JobCategory> jobList;
}
