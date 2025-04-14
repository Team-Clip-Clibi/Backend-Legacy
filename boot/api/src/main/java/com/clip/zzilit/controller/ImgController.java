package com.clip.zzilit.controller;

import com.clip.infra.aws.s3.S3ImgService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class ImgController {
    private final S3ImgService s3ImgService;
    @PostMapping("/zzilit/img/upload")
    public String uploadImg(@RequestParam("img") MultipartFile img) {
        return s3ImgService.getPublicReadUrlAfterUpload(img);
    }
}
