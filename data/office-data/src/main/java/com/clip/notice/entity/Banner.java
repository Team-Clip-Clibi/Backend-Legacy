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
    private BannerType bannerType;
    private String head;
    private String sub;
    private String imageUrl;
    private LocalDate exposureDate;
    private boolean isExposure;

    @Builder
    public Banner(BannerType bannerType, String head, String sub, String imageUrl, LocalDate exposureDate, boolean isExposure) {
        this.bannerType = bannerType;
        this.head = head;
        this.sub = sub;
        this.imageUrl = imageUrl;
        this.exposureDate = exposureDate;
        this.isExposure = isExposure;
    }
}
