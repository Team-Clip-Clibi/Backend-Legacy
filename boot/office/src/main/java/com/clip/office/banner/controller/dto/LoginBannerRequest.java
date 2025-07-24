package com.clip.office.banner.controller.dto;

import java.time.LocalDateTime;

public record LoginBannerRequest(
        String imgName,
        String text,
        LocalDateTime exposureDate
) {
}
