package com.clip.office.notice.controller.mapper;

import com.clip.notice.entity.Notice;
import com.clip.office.notice.controller.dto.NoticeInfo;
import org.springframework.stereotype.Component;

@Component
public class NoticeMapper {
    public NoticeInfo toNoticeInfo(Notice notice) {
        return new NoticeInfo(notice.getId(), notice.getText(), notice.getNoticeType(), notice.getExposureDateTime());
    }
}
