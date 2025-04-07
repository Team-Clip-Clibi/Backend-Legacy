package com.clip.notice.entity;

import com.clip.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    private String content;

    private String link;

    private LocalDate exposureDate;

    private boolean isExposure;

    @Builder
    public Notice(String content, String link, LocalDate exposureDate, boolean isExposure) {
        this.content = content;
        this.link = link;
        this.exposureDate = exposureDate;
        this.isExposure = isExposure;
    }
}
