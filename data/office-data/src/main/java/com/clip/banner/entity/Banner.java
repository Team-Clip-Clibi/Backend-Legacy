package com.clip.banner.entity;

import com.clip.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    private String imgName;
    private LocalDateTime exposureDatetime;

    @Builder
    public Banner(BannerType bannerType, String text, String imgName, LocalDateTime exposureDatetime) {
        this.bannerType = bannerType;
        this.text = text;
        this.imgName = imgName;
        this.exposureDatetime = exposureDatetime;
    }

    public void updateImageUrl(String imageUrl) {
        this.imgName = imageUrl;
    }

    public void update(BannerType bannerType, String text, LocalDateTime exposureDate) {
        this.bannerType = bannerType;
        this.text = text;
        this.exposureDatetime = exposureDate;
    }
}
