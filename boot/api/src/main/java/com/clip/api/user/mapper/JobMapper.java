package com.clip.api.user.mapper;

import com.clip.api.user.controller.dto.JobCategory;
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
}
