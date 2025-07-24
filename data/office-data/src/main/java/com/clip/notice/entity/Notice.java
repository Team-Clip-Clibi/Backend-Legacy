package com.clip.notice.entity;

import com.clip.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Notice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private NoticeType noticeType;

    private String text;

    private LocalDateTime exposureDateTime;

    @Builder
    public Notice(NoticeType noticeType, String text, LocalDateTime exposureDateTime) {
        this.noticeType = noticeType;
        this.text = text;
        this.exposureDateTime = exposureDateTime;
    }

    public void updateNotice(NoticeType noticeType,String content, LocalDateTime exposureDate) {
        this.noticeType = noticeType;
        this.text = content;
        this.exposureDateTime = exposureDate;
    }
}