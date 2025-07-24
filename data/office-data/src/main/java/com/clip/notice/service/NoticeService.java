package com.clip.notice.service;

import com.clip.notice.entity.Notice;
import com.clip.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;

    @Transactional
    public Notice save(Notice notice) {
        return noticeRepository.save(notice);
    }

    @Transactional(readOnly = true)
    public List<Notice> findAllNotices() {
        return noticeRepository.findAll();
    }

    @Transactional
    public void deleteById(Long noticeId) {
        noticeRepository.deleteById(noticeId);
    }

    @Transactional(readOnly = true)
    public List<Notice> findExposedNotice() {
        return noticeRepository.findExposedNotice(LocalDateTime.now());
    }
}
