package com.clip.office.notice.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class UpdateNewsDto {

    private Long id;
    private String content;
    private String link;
    private LocalDate exposureDate;
    private boolean isExposure;

    @Builder
    public UpdateNewsDto(Long id,String content, String link, LocalDate exposureDate, boolean isExposure) {
        this.id = id;
        this.content = content;
        this.link = link;
        this.exposureDate = exposureDate;
        this.isExposure = isExposure;
    }
}
