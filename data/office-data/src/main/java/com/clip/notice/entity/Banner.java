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
public class Banner extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private BannerType bannerType;
    private String text;
    private String imageUrl;
    private LocalDate exposureDate;
    private boolean isExposure;

    @Builder
    public Banner(BannerType bannerType, String text, String imageUrl, LocalDate exposureDate, boolean isExposure) {
        this.bannerType = bannerType;
        this.text = text;
        this.imageUrl = imageUrl;
        this.exposureDate = exposureDate;
        this.isExposure = isExposure;
    }

    public void updateImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void update(BannerType bannerType, String text, LocalDate exposureDate, boolean exposure) {
        this.bannerType = bannerType;
        this.text = text;
        this.exposureDate = exposureDate;
        this.isExposure = exposure;
    }
}
