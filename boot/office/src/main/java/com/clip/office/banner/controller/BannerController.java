package com.clip.office.banner.controller;

import com.clip.office.banner.controller.dto.BannerInfo;
import com.clip.office.banner.controller.dto.HomeBannerRequest;
import com.clip.office.banner.controller.dto.LoginBannerRequest;
import com.clip.office.banner.controller.dto.ImgUrlInfo;
import com.clip.office.banner.service.BannerAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/banners")
@RequiredArgsConstructor
public class BannerController {

    private final BannerAdminService bannerAdminService;

    @GetMapping("/upload-url")
    public ImgUrlInfo getImgUploadUrl() {
        return bannerAdminService.getBannerUploadUrl();
    }

    @GetMapping("/login")
    public List<BannerInfo> getLoginBannerList() {
        return bannerAdminService.getLoginBannerList();
    }

    @GetMapping("/home")
    public List<BannerInfo> getHomeBannerList() {
        return bannerAdminService.getHomeBannerList();
    }

    @DeleteMapping("/login/{bannerId}")
    public void deleteLoginBanner(@PathVariable long bannerId) {
        bannerAdminService.deleteBanner(bannerId);
    }

    @DeleteMapping("/home/{bannerId}")
    public void deleteHomeBanner(@PathVariable long bannerId) {
        bannerAdminService.deleteBanner(bannerId);
    }

    @PostMapping("/login")
    public void saveLoginBanner(@RequestBody LoginBannerRequest request) {
        bannerAdminService.saveLoginBanner(request);
    }

    @PostMapping("/home")
    public void saveHomeBanner(@RequestBody HomeBannerRequest request) {
        bannerAdminService.saveHomeBanner(request);
    }

}
