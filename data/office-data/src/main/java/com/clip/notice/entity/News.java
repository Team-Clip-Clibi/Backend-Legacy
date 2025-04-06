package com.clip.notice.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private String link;

    private LocalDate exposureDate;

    private boolean isExposure;

    @Builder
    public News(String content, String link, LocalDate exposureDate, boolean isExposure) {
        this.content = content;
        this.link = link;
        this.exposureDate = exposureDate;
        this.isExposure = isExposure;
    }
}
