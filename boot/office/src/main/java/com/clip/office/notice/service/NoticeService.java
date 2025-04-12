package com.clip.office.notice.service;

import com.clip.notice.entity.Notice;
import com.clip.notice.service.NoticeDataService;
import com.clip.office.notice.controller.dto.CreateNoticeDto;
import com.clip.office.notice.controller.dto.UpdateNoticeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeDataService noticeDataService;

    @Transactional
    public CreateNoticeDto createNotice(CreateNoticeDto createNoticeDto) {

        Notice notice = Notice.builder()
                .noticeType(createNoticeDto.getNoticeType())
                .content(createNoticeDto.getContent())
                .link(createNoticeDto.getLink())
                .exposureDate(createNoticeDto.getExposureDate())
                .isExposure(false)
                .build();

        noticeDataService.save(notice);

        return CreateNoticeDto.builder()
                .id(notice.getId())
                .noticeType(notice.getNoticeType())
                .content(notice.getContent())
                .link(notice.getLink())
                .exposureDate(notice.getExposureDate())
                .build();
    }

    @Transactional
    public UpdateNoticeDto updateNotice(Long noticeId, UpdateNoticeDto updateNoticeDto) {
        Notice notice = noticeDataService.findNotice(noticeId);

        notice.updateNotice(
                updateNoticeDto.getNoticeType(),
                updateNoticeDto.getContent(),
                updateNoticeDto.getLink(),
                updateNoticeDto.getExposureDate(),
                updateNoticeDto.isExposure()
        );

        return updateNoticeDto.builder()
                .id(notice.getId())
                .noticeType(notice.getNoticeType())
                .content(notice.getContent())
                .link(notice.getLink())
                .exposureDate(notice.getExposureDate())
                .isExposure(notice.isExposure())
                .build();
    }

    public void deleteNotice(Long noticeId) {
        noticeDataService.deleteNotice(noticeId);
    }
}
