package com.clip.api.display.controller.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BannerInfoDto {

    private String imagePresignedUrl;
    private String headText;
    private String subText;

    @Builder
    public BannerInfoDto(String imagePresignedUrl, String headText, String subText) {
        this.imagePresignedUrl = imagePresignedUrl;
        this.headText = headText;
        this.subText = subText;
    }

}
