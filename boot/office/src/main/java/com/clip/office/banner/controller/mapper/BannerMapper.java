package com.clip.office.banner.controller.mapper;

import com.clip.banner.entity.Banner;
import com.clip.office.banner.controller.dto.BannerInfo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BannerMapper {
    public List<BannerInfo> toBannerInfos(List<Banner> banners) {
        return banners.stream()
                .map(banner -> new BannerInfo(
                        banner.getId(),
                        banner.getExposureDatetime(),
                        banner.getText())
                ).toList();
    }
}
