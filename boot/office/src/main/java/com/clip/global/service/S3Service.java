package com.clip.global.service;

import com.amazonaws.services.s3.AmazonS3;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String BUCKET;

    public String imageUpload(MultipartFile file) {
        if (file.getSize() > 50 * 1024 * 1024) { // 50MB
            throw new IllegalArgumentException("파일 크기가 너무 큽니다.");
        }

        if (file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어 있습니다.");
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        try {
            s3Client.putObject(BUCKET, fileName, file.getInputStream(), null);
        } catch (IOException e) {
            throw new RuntimeException("S3 업로드 실패", e);
        }

        return getFileUrl(fileName);
    }

    private String getFileUrl(String fileName) {
        return s3Client.getUrl(BUCKET, fileName).toString();
    }
}
