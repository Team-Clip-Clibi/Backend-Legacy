package com.clip.office.notice.controller;


import com.clip.office.notice.controller.dto.NoticeInfo;
import com.clip.office.notice.controller.dto.NoticeRequest;
import com.clip.office.notice.service.NoticeAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notices")
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeAdminService noticeAdminService;

    @GetMapping
    public List<NoticeInfo> getNoticeList() {
        return noticeAdminService.getNoticeList();
    }

    @DeleteMapping("/{noticeId}")
    public void deleteNotice(@PathVariable Long noticeId) {
        noticeAdminService.deleteNotice(noticeId);
    }

    @PostMapping
    public void saveNotice(@RequestBody NoticeRequest request) {
        noticeAdminService.saveNotice(request);
    }
}
