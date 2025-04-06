package com.clip.notification.service;

import com.clip.notification.entity.Banner;
import com.clip.notification.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BannerDataService {

    private final BannerRepository bannerRepository;

    public Banner save(Banner banner) {
        return bannerRepository.save(banner);
    }
}