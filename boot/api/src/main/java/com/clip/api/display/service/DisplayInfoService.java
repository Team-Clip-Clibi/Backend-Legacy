package com.clip.api.display.service;

import com.clip.api.display.controller.dto.BannerInfoDto;
import com.clip.api.display.controller.dto.NoticeInfoDto;
import com.clip.infra.aws.s3.S3ImgService;
import com.clip.notice.entity.Banner;
import com.clip.notice.entity.BannerType;
import com.clip.notice.service.BannerDataService;
import com.clip.notice.service.NoticeDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DisplayInfoService {
    private final BannerDataService bannerDataService;
    private final NoticeDataService noticeDataService;
    private final S3ImgService s3ImgService;

    public List<BannerInfoDto> getBanners(final BannerType bannerType) {
        List<Banner> banners = bannerDataService.findBanners(bannerType);
        return banners.stream()
                .map(banner -> BannerInfoDto.builder()
                        .imagePresignedUrl(s3ImgService.generateGetPresignedUrl(banner.getImageUrl()))
                        .text(banner.getText().replace("\\n", "\n"))
                        .build())
                .toList();
    }

    public List<NoticeInfoDto> getNotices() {
        return noticeDataService.findNotices().stream()
                .map(notice -> NoticeInfoDto.builder()
                        .noticeType(notice.getNoticeType())
                        .content(notice.getContent())
                        .link(notice.getLink())
                        .build())
                .toList();
    }

}
