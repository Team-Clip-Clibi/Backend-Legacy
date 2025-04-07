package com.clip.office.notice.controller.dto;

import com.clip.notice.entity.BannerType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class CreateBannerDto {

    private BannerType bannerType;
    private String head;
    private String sub;
    private String imageUrl;
    private LocalDate exposureDate;
    private boolean isExposure;

    @Builder
    public CreateBannerDto(BannerType bannerType, String head, String sub,String imageUrl, LocalDate exposureDate, boolean isExposure) {
        this.bannerType = bannerType;
        this.head = head;
        this.sub = sub;
        this.imageUrl = imageUrl;
        this.exposureDate = exposureDate;
        this.isExposure = isExposure;
    }

}
