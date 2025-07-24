package com.clip.office.notice.service;

import com.clip.notice.entity.Notice;
import com.clip.notice.service.NoticeService;
import com.clip.office.notice.controller.dto.NoticeInfo;
import com.clip.office.notice.controller.dto.NoticeRequest;
import com.clip.office.notice.controller.mapper.NoticeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeAdminService {
    private final NoticeService noticeService;
    private final NoticeMapper noticeMapper;

    @Transactional(readOnly = true)
    public List<NoticeInfo> getNoticeList() {
        return noticeService.findAllNotices()
                .stream()
                .map(noticeMapper::toNoticeInfo)
                .toList();
    }

    @Transactional
    public void deleteNotice(Long noticeId) {
        noticeService.deleteById(noticeId);
    }

    @Transactional
    public void saveNotice(NoticeRequest request) {
        noticeService.save(Notice.builder()
                .noticeType(request.noticeType())
                .text(request.text())
                .exposureDateTime(request.exposureDate())
                .build());
    }
}
