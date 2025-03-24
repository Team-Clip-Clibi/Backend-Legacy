package com.clip.api.user.mapper;

import com.clip.api.user.controller.dto.JobCategory;
import com.clip.api.user.controller.dto.JobDto;
import com.clip.user.entity.Job;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface JobMapper {
    default List<String> toStringJobList(List<JobCategory> jobCategoryList) {
        return jobCategoryList.stream()
                .map(JobCategory::getJobCategoryName)
                .toList();
    }

    default JobDto toJobDto(List<Job> jobList) {
        return JobDto.builder()
                .jobList(
                        jobList.stream()
                                .map(job -> {
                                    for (JobCategory category : JobCategory.values()) {
                                        if (category.getJobCategoryName().equals(job.getJobName())) {
                                            return category;
                                        }
                                    }
                                    throw new IllegalArgumentException("다음 이름은 DTO로 변환할 수 없는 이름입니다: " + job.getJobName());
                                })
                                .toList()
                )
                .build();
    }
}
