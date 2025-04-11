package com.clip.office.notice.service;

import com.clip.global.service.S3Service;
import com.clip.notice.entity.Banner;
import com.clip.notice.service.BannerDataService;
import com.clip.office.notice.controller.dto.CreateBannerDto;
import com.clip.office.notice.controller.dto.UpdateBannerDto;
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
                .bannerType(dto.getBannerType())
                .head(dto.getHead())
                .sub(dto.getSub())
                .imageUrl(imageUrl)
                .exposureDate(dto.getExposureDate())
                .isExposure(dto.isExposure())
                .build();

        bannerDataService.save(banner);

        return CreateBannerDto.builder()
                .id(banner.getId())
                .bannerType(banner.getBannerType())
                .head(banner.getHead())
                .sub(banner.getSub())
                .imageUrl(banner.getImageUrl())
                .exposureDate(banner.getExposureDate())
                .isExposure(banner.isExposure())
                .build();
    }

    @Transactional
    public UpdateBannerDto updateBanner(Long bannerId, UpdateBannerDto updateBannerDto, MultipartFile file) {
        Banner banner = bannerDataService.findBanner(bannerId);

        if (file != null && !file.isEmpty()) {
            String imageUrl = s3Service.imageUpload(file);
            banner.updateImageUrl(imageUrl);
        }

        banner.update(
                updateBannerDto.getBannerType(),
                updateBannerDto.getHead(),
                updateBannerDto.getSub(),
                updateBannerDto.getExposureDate(),
                updateBannerDto.isExposure()
        );

        return UpdateBannerDto.builder()
                .id(banner.getId())
                .bannerType(banner.getBannerType())
                .head(banner.getHead())
                .sub(banner.getSub())
                .imageUrl(banner.getImageUrl())
                .exposureDate(banner.getExposureDate())
                .isExposure(banner.isExposure())
                .build();
    }

    public void deleteBanner(Long bannerId) {
        Banner banner = bannerDataService.findBanner(bannerId);
        s3Service.deleteImage(banner.getImageUrl());
        bannerDataService.delete(bannerId);
    }
}
