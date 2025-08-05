package com.clip.office.banner.service;

import com.clip.infra.aws.s3.S3ImgService;
import com.clip.infra.aws.s3.config.S3PathProperties;
import com.clip.banner.entity.Banner;
import com.clip.banner.entity.BannerType;
import com.clip.banner.service.BannerService;
import com.clip.office.banner.controller.dto.BannerInfo;
import com.clip.office.banner.controller.dto.HomeBannerRequest;
import com.clip.office.banner.controller.dto.LoginBannerRequest;
import com.clip.office.banner.controller.mapper.BannerMapper;
import com.clip.office.banner.controller.dto.ImgUrlInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BannerAdminService {
    private final BannerService bannerService;
    private final BannerMapper bannerMapper;
    private final S3ImgService s3ImgService;
    private final S3PathProperties s3PathProperties;

    @Transactional(readOnly = true)
    public List<BannerInfo> getLoginBannerList() {
        return bannerMapper.toBannerInfos(bannerService.findAllLoginBanners());
    }

    @Transactional(readOnly = true)
    public List<BannerInfo> getHomeBannerList() {
        return bannerMapper.toBannerInfos(bannerService.findAllHomeBanners());
    }

    @Transactional
    public void deleteBanner(long bannerId) {
        Banner banner = bannerService.findById(bannerId);
        s3ImgService.deleteImg(s3PathProperties.getBANNER_IMG_PATH(), banner.getImgName());
        bannerService.deleteByBanner(banner);
    }

    public ImgUrlInfo getBannerUploadUrl() {
        String imgName = UUID.randomUUID() + ".svg";
        String getPresignedUrl = s3ImgService.generatePutPresignedUrl(s3PathProperties.getBANNER_IMG_PATH(), imgName);
        return new ImgUrlInfo(imgName, getPresignedUrl);
    }

    public void saveLoginBanner(LoginBannerRequest request) {
        if (!s3ImgService.isImgSaved(s3PathProperties.getBANNER_IMG_PATH(), request.imgName())) {
            throw new IllegalArgumentException("이미지 파일이 존재하지 않습니다.");
        }

        bannerService.save(Banner.builder()
                .bannerType(BannerType.LOGIN)
                .text(request.text())
                .imgName(request.imgName())
                .exposureDatetime(request.exposureDate())
                .build());
    }

    public void saveHomeBanner(HomeBannerRequest request) {
        if (!s3ImgService.isImgSaved(s3PathProperties.getBANNER_IMG_PATH(), request.imgName())) {
            throw new IllegalArgumentException("이미지 파일이 존재하지 않습니다.");
        }

        bannerService.save(Banner.builder()
                .bannerType(BannerType.HOME)
                .text(null)
                .imgName(request.imgName())
                .exposureDatetime(request.exposureDate())
                .build());
    }
}
