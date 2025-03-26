package com.clip.api.report.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReportDto {
    private String content;

    @Builder
    public ReportDto(String content) {
        this.content = content;
    }
}
