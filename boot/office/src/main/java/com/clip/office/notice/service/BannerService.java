package com.clip.office.notice.service;

import com.clip.infra.aws.s3.S3ImgService;
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
    private final S3ImgService s3ImgService;

    @Transactional
    public CreateBannerDto createBanner(CreateBannerDto dto, MultipartFile file) {
        String imageUrl = s3ImgService.imageUpload(file);

        Banner banner = Banner.builder()
                .bannerType(dto.getBannerType())
                .text(dto.getText())
                .imageUrl(imageUrl)
                .exposureDate(dto.getExposureDate())
                .isExposure(dto.isExposure())
                .build();

        bannerDataService.save(banner);

        return CreateBannerDto.builder()
                .id(banner.getId())
                .bannerType(banner.getBannerType())
                .text(banner.getText())
                .imageUrl(banner.getImageUrl())
                .exposureDate(banner.getExposureDate())
                .isExposure(banner.isExposure())
                .build();
    }

    @Transactional
    public UpdateBannerDto updateBanner(Long bannerId, UpdateBannerDto updateBannerDto, MultipartFile file) {
        Banner banner = bannerDataService.findBanner(bannerId);

        if (file != null && !file.isEmpty()) {
            String imageUrl = s3ImgService.imageUpload(file);
            banner.updateImageUrl(imageUrl);
        }

        banner.update(
                updateBannerDto.getBannerType(),
                updateBannerDto.getText(),
                updateBannerDto.getExposureDate(),
                updateBannerDto.isExposure()
        );

        return UpdateBannerDto.builder()
                .id(banner.getId())
                .bannerType(banner.getBannerType())
                .text(banner.getText())
                .imageUrl(banner.getImageUrl())
                .exposureDate(banner.getExposureDate())
                .isExposure(banner.isExposure())
                .build();
    }

    public void deleteBanner(Long bannerId) {
        Banner banner = bannerDataService.findBanner(bannerId);
        s3ImgService.deleteImage(banner.getImageUrl());
        bannerDataService.delete(bannerId);
    }
}
