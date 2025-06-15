package com.clip.api.display.controller.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BannerInfoDto {

    private String imagePresignedUrl;
    private String text;

    @Builder
    public BannerInfoDto(String imagePresignedUrl, String text) {
        this.imagePresignedUrl = imagePresignedUrl;
        this.text = text;
    }

}
