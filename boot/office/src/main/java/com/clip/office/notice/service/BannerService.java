package com.clip.office.notice.service;

import com.clip.global.service.S3Service;
import com.clip.notification.entity.Banner;
import com.clip.notification.service.BannerDataService;
import com.clip.office.notice.controller.dto.CreateBannerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BannerService {

    private final BannerDataService bannerDataService;
    private final S3Service s3Service;

    @Transactional
    public CreateBannerDto createBanner(CreateBannerDto dto, MultipartFile file) {
        String imageUrl = s3Service.imageUpload(file);

        Banner banner = Banner.builder()
                .head(dto.getHead())
                .sub(dto.getSub())
                .imageUrl(imageUrl)
                .exposureLocation(dto.getExposureLocation())
                .exposureDate(dto.getExposureDate())
                .isExposure(dto.isExposure())
                .build();

        bannerDataService.save(banner);

        return CreateBannerDto.builder()
                .head(banner.getHead())
                .sub(banner.getSub())
                .imageUrl(banner.getImageUrl())
                .exposureLocation(banner.getExposureLocation())
                .exposureDate(banner.getExposureDate())
                .isExposure(banner.isExposure())
                .build();
    }
}
