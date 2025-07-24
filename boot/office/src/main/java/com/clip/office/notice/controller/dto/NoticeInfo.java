package com.clip.office.notice.controller.dto;

import com.clip.notice.entity.NoticeType;

import java.time.LocalDateTime;

public record NoticeInfo(
        long no,
        String text,
        NoticeType noticeType,
        LocalDateTime exposureDate) {
}
