package com.clip.office.notice.controller;

import com.clip.office.notice.controller.dto.CreateBannerDto;
import com.clip.office.notice.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/office/notification/banner")
@RequiredArgsConstructor
public class BannerController {

    private final BannerService bannerService;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CreateBannerDto> createBanner(
            @RequestPart(value = "data", required = true) CreateBannerDto createBannerDto,
            @RequestPart(value = "image", required = true) MultipartFile file
    ) {
        return ResponseEntity.ok(bannerService.createBanner(createBannerDto, file));
    }

}
