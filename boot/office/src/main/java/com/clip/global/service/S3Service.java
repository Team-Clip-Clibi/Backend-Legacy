package com.clip.global.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String BUCKET;

    @Value("${cloud.aws.s3.region}")
    private String REGION;

    @Value("${spring.servlet.multipart.max-file-size}")
    private String MAXFILESIZE;

    public String imageUpload(MultipartFile file) {
        long maxFileSizeInBytes = parseToBytes(MAXFILESIZE);

        if (file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어 있습니다.");
        }

        if (!isValidImageFile(file)) {
            throw new IllegalArgumentException("이미지 파일만 업로드 가능합니다.");
        }

        if (file.getSize() > maxFileSizeInBytes ) {
            throw new IllegalArgumentException("파일 크기가 너무 큽니다.");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isBlank()) {
            throw new IllegalArgumentException("파일 이름이 비어 있습니다.");
        }

        String fileName = UUID.randomUUID() + "_" + originalFilename;

        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(BUCKET)
                    .key(fileName)
                    .contentType(Optional.ofNullable(file.getContentType()).orElse("application/octet-stream"))
                    .build();

            s3Client.putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        } catch (IOException e) {
            throw new RuntimeException("S3 업로드 실패", e);
        }

        return getFileUrl(fileName);
    }

    private String getFileUrl(String fileName) {
        return "https://" + BUCKET + ".s3." + REGION + ".amazonaws.com/" + fileName;
    }

    private long parseToBytes(String size) {
        size = size.toUpperCase();
        if (size.endsWith("MB")) {
            return Long.parseLong(size.replace("MB", "").trim()) * 1024 * 1024;
        }
        if (size.endsWith("KB")) {
            return Long.parseLong(size.replace("KB", "").trim()) * 1024;
        }
        if (size.endsWith("B")) {
            return Long.parseLong(size.replace("B", "").trim());
        }
        return Long.parseLong(size.trim());
    }


    private boolean isValidImageFile(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            return false;
        }

        String lowerCaseFilename = originalFilename.toLowerCase();
        return lowerCaseFilename.endsWith(".jpg")
                || lowerCaseFilename.endsWith(".jpeg")
                || lowerCaseFilename.endsWith(".png")
                || lowerCaseFilename.endsWith(".gif")
                || lowerCaseFilename.endsWith(".bmp");
    }
}
