package com.clip.notice.service;

import com.clip.notice.entity.Banner;
import com.clip.notice.entity.BannerType;
import com.clip.notice.exception.BannerNotFoundException;
import com.clip.notice.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerDataService {

    private final BannerRepository bannerRepository;

    public Banner save(Banner banner) {
        return bannerRepository.save(banner);
    }

    public Banner findBanner(Long bannerId) {
        return bannerRepository.findBanner(bannerId)
                .orElseThrow(BannerNotFoundException::new);
    }

    public void delete(Long bannerId) {
        bannerRepository.deleteBanner(bannerId);
    }

    public List<Banner> findBanners(final BannerType bannerType) {
        return bannerRepository.findByBannerType(bannerType, LocalDate.now());
    }
}
