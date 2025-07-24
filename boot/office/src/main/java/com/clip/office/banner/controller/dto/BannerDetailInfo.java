package com.clip.office.banner.controller.dto;

import java.time.LocalDateTime;

public record BannerDetailInfo(
        long no,
        LocalDateTime exposureDate,
        String text,
        boolean isExposure
) {
}
