package com.clip.office.notice.controller.dto;

import com.clip.notice.entity.NoticeType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class UpdateNoticeDto {

    private Long id;
    private NoticeType noticeType;
    private String content;
    private String link;
    private LocalDate exposureDate;
    private boolean isExposure;

    @Builder
    public UpdateNoticeDto(Long id,NoticeType noticeType ,String content, String link, LocalDate exposureDate, boolean isExposure) {
        this.id = id;
        this.noticeType = noticeType;
        this.content = content;
        this.link = link;
        this.exposureDate = exposureDate;
        this.isExposure = isExposure;
    }
}
