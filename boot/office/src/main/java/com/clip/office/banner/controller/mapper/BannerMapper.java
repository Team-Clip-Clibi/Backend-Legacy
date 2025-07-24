package com.clip.office.banner.controller.mapper;

import com.clip.banner.entity.Banner;
import com.clip.office.banner.controller.dto.BannerInfo;
import org.springframework.stereotype.Component;

@Component
public class BannerMapper {
    public BannerInfo toBannerInfo(Banner banner) {
        return new BannerInfo(
                banner.getId(),
                banner.getExposureDatetime(),
                banner.getText()
        );
    }
}
