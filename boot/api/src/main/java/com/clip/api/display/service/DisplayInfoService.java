package com.clip.api.display.service;

import com.clip.api.display.controller.dto.BannerInfoDto;
import com.clip.api.display.controller.dto.NoticeInfoDto;
import com.clip.banner.service.BannerService;
import com.clip.infra.aws.s3.S3ImgService;
import com.clip.banner.entity.Banner;
import com.clip.banner.entity.BannerType;
import com.clip.infra.aws.s3.config.S3PathProperties;
import com.clip.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DisplayInfoService {
    private final BannerService bannerService;
    private final NoticeService noticeService;
    private final S3ImgService s3ImgService;
    private final S3PathProperties s3PathProperties;

    public List<BannerInfoDto> getBanners(final BannerType bannerType) {
        List<Banner> banners = bannerService.findBanners(bannerType);
        return banners.stream()
                .map(banner -> BannerInfoDto.builder()
                        .imagePresignedUrl(s3ImgService.generateGetPresignedUrl(s3PathProperties.getBANNER_IMG_PATH(), banner.getImgName()))
                        .text(banner.getText() != null ? banner.getText().replace("\\n", "\n") : null)
                        .build())
                .toList();
    }

    public List<NoticeInfoDto> getNotices() {
        return noticeService.findExposedNotice().stream()
                .map(notice -> NoticeInfoDto.builder()
                        .noticeType(notice.getNoticeType())
                        .content(notice.getText())
                        .build())
                .toList();
    }

}
