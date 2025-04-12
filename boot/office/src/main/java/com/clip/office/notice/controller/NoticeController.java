package com.clip.office.notice.controller;

import com.clip.office.notice.controller.dto.CreateNoticeDto;
import com.clip.office.notice.controller.dto.UpdateNoticeDto;
import com.clip.office.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/office/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @PostMapping("/create")
    public ResponseEntity<CreateNoticeDto> createNotice(
            @RequestBody CreateNoticeDto createNoticeDto
    ) {
        return ResponseEntity.ok(noticeService.createNotice(createNoticeDto));
    }

    @PutMapping("/{noticeId}/update")
    public ResponseEntity<UpdateNoticeDto> updateNotice(
            @PathVariable(value = "noticeId") Long noticeId,
            @RequestBody UpdateNoticeDto updateNoticeDto
    ) {
        return ResponseEntity.ok(noticeService.updateNotice(noticeId, updateNoticeDto));
    }

    @DeleteMapping("/{noticeId}/delete")
    public ResponseEntity<Void> deleteNotice(
            @PathVariable(value = "noticeId") Long noticeId
    ) {
        noticeService.deleteNotice(noticeId);
        return ResponseEntity.ok().build();
    }
}
