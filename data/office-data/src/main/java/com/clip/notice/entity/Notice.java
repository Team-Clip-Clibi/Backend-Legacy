package com.clip.notice.entity;

import com.clip.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class Notice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private NoticeType noticeType;

    private String content;

    private String link;

    private LocalDate exposureDate;

    private boolean isExposure;

    @Builder
    public Notice(NoticeType noticeType,String content, String link, LocalDate exposureDate, boolean isExposure) {
        this.noticeType = noticeType;
        this.content = content;
        this.link = link;
        this.exposureDate = exposureDate;
        this.isExposure = isExposure;
    }

    public void updateNotice(NoticeType noticeType,String content, String link, LocalDate exposureDate, boolean isExposure) {
        this.noticeType = noticeType;
        this.content = content;
        this.link = link;
        this.exposureDate = exposureDate;
        this.isExposure = isExposure;
    }
}