package com.clip.api.display.controller.dto;

import com.clip.notice.entity.NoticeType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NoticeInfoDto {
    private NoticeType noticeType;
    private String content;
    private String link;

    @Builder
    public NoticeInfoDto(NoticeType noticeType, String content, String link) {
        this.noticeType = noticeType;
        this.content = content;
        this.link = link;
    }
}
