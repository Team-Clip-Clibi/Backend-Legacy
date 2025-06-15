package com.clip.office.notice.controller.dto;

import com.clip.notice.entity.BannerType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class CreateBannerDto {

    private Long id;
    private BannerType bannerType;
    private String text;
    private String imageUrl;
    private LocalDate exposureDate;
    private boolean isExposure;

    @Builder
    public CreateBannerDto(Long id,BannerType bannerType, String text, String imageUrl, LocalDate exposureDate, boolean isExposure) {
        this.id = id;
        this.bannerType = bannerType;
        this.text = text;
        this.imageUrl = imageUrl;
        this.exposureDate = exposureDate;
        this.isExposure = isExposure;
    }

}
