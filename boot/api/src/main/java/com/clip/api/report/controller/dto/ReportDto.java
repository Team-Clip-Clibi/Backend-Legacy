package com.clip.api.report.controller.dto;

import com.clip.report.entity.ReportCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReportDto {
    private String content;
    private ReportCategory reportCategory;

    @Builder
    public ReportDto(String content, ReportCategory reportCategory) {
        this.content = content;
        this.reportCategory = reportCategory;
    }
}
