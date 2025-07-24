package com.clip.office.banner.controller.dto;

import java.time.LocalDateTime;

public record HomeBannerRequest(
        String imgName,
        LocalDateTime exposureDate
) {
}
