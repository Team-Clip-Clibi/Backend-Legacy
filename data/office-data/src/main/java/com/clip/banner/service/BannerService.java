package com.clip.banner.service;

import com.clip.banner.entity.Banner;
import com.clip.banner.entity.BannerType;
import com.clip.banner.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerService {
    private final BannerRepository bannerRepository;


    @Transactional(readOnly = true)
    public List<Banner> findAllLoginBanners() {
        return bannerRepository.findByBannerType(BannerType.LOGIN);
    }

    @Transactional(readOnly = true)
    public List<Banner> findAllHomeBanners() {
        return bannerRepository.findByBannerType(BannerType.HOME);
    }

    @Transactional
    public void deleteByBanner(Banner banner) {
        bannerRepository.delete(banner);
    }

    @Transactional(readOnly = true)
    public Banner findById(long bannerId) {
        return bannerRepository.findById(bannerId)
                .orElseThrow(() -> new IllegalArgumentException("Banner not found with id: " + bannerId));
    }

    @Transactional
    public Banner save(Banner banner) {
        return bannerRepository.save(banner);
    }

    @Transactional(readOnly = true)
    public List<Banner> findBanners(BannerType bannerType) {
        return bannerRepository.findByBannerType(bannerType, LocalDateTime.now());
    }
}
