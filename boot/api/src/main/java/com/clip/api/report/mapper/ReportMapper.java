package com.clip.api.report.mapper;

import com.clip.api.report.controller.dto.ReportDto;
import com.clip.report.entity.Report;
import com.clip.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReportMapper {
    Report toReport(User user, ReportDto reportDto);
}
