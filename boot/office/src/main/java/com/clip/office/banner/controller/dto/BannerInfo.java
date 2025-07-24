package com.clip.office.banner.controller.dto;

import java.time.LocalDateTime;

public record BannerInfo(
        long no,
        LocalDateTime exposureDate,
        String text
) {
}
