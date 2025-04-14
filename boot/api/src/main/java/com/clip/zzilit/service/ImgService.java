package com.clip.zzilit.service;

import com.clip.infra.aws.s3.S3ImgService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImgService {
    private final S3ImgService s3ImgService;
    public String uploadToS3(MultipartFile img) {
        return null;
    }
}
