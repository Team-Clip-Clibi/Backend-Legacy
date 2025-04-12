package com.clip.notice.service;

import com.clip.notice.entity.Notice;
import com.clip.notice.exception.NoticeNotFoundException;
import com.clip.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoticeDataService {

    private final NoticeRepository noticeRepository;

    public Notice findNotice(Long noticeId) {
        return noticeRepository.findNotice(noticeId)
                .orElseThrow(NoticeNotFoundException::new);
    }

    public Notice save(Notice notice) {
        return noticeRepository.save(notice);
    }

    public void deleteNotice(Long noticeId) {
        noticeRepository.deleteNotice(noticeId);
    }
}
