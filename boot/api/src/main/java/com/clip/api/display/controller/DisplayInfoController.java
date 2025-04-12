package com.clip.api.display.controller;

import com.clip.api.display.controller.dto.BannerInfoDto;
import com.clip.api.display.service.DisplayInfoService;
import com.clip.api.docs.display.DisplayInfoDocs;
import com.clip.notice.entity.BannerType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DisplayInfoController implements DisplayInfoDocs {
    private final DisplayInfoService displayInfoService;

    @Override
    public List<BannerInfoDto> getBanners(final BannerType bannerType) {
        return displayInfoService.getBanners(bannerType);
    }
}
