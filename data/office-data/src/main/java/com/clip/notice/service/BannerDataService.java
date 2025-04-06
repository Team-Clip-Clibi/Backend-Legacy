package com.clip.notice.service;

import com.clip.notice.entity.Banner;
import com.clip.notice.repository.BannerRepository;
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