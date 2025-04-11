package com.clip.office.notice.controller;

import com.clip.office.notice.controller.dto.CreateBannerDto;
import com.clip.office.notice.controller.dto.UpdateBannerDto;
import com.clip.office.notice.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/office/notice/banner")
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

    @PutMapping(value = "/{bannerId}/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UpdateBannerDto> updateBanner(
            @PathVariable(value = "bannerId") Long bannerId,
            @RequestPart(value = "data", required = true) UpdateBannerDto updateBannerDto,
            @RequestPart(value = "image", required = true) MultipartFile file
    ) {
        return ResponseEntity.ok(bannerService.updateBanner(bannerId, updateBannerDto, file));
    }

    @DeleteMapping("/{bannerId}/delete")
    public ResponseEntity<Void> deleteBanner(
            @PathVariable(value = "bannerId") Long bannerId
    ) {
        bannerService.deleteBanner(bannerId);
        return ResponseEntity.ok().build();
    }
}
