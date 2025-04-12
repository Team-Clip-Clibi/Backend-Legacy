package com.clip.infra.aws.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3ImgService {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.region}")
    private String region;

    private static final Duration PRESIGNED_URL_EXPIRY_TIME = Duration.ofHours(24);
    private static final List<String> ALLOWED_EXTENSIONS = List.of(".jpg", ".jpeg", ".png", ".gif", ".bmp");
    private static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";

    /**
     * 이미지 파일을 S3에 업로드하고 URL을 반환
     */
    public String imageUpload(MultipartFile file) {
        validateFile(file);
        String fileName = generateUniqueFileName(file.getOriginalFilename());
        uploadToS3(file, fileName);
        return generateS3Url(fileName);
    }

    /**
     * 이미지 URL로부터 Presigned URL을 생성
     */
    public String generateGetPresignedUrl(final String imgUrl) {
        String key = extractKeyFromUrl(imgUrl);
        return createPresignedGetUrl(key);
    }

    public void deleteImage(String imgUrl) {
        String key = extractKeyFromUrl(imgUrl);
        s3Client.deleteObject(b -> b.bucket(bucket).key(key));
    }

    private String createPresignedGetUrl(String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .getObjectRequest(getObjectRequest)
                .signatureDuration(PRESIGNED_URL_EXPIRY_TIME)
                .build();

        return s3Presigner.presignGetObject(presignRequest)
                .url()
                .toString();
    }

    private void validateFile(MultipartFile file) {
        validateFileNotEmpty(file);
        validateFileName(file);
        validateFileExtension(file);
    }

    private void validateFileNotEmpty(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어 있습니다.");
        }
    }

    private void validateFileName(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isBlank()) {
            throw new IllegalArgumentException("파일 이름이 비어 있습니다.");
        }
    }

    private void validateFileExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null && !isValidImageFile(originalFilename)) {
            throw new IllegalArgumentException("이미지 파일만 업로드 가능합니다.");
        }
    }

    private boolean isValidImageFile(String filename) {
        String lowerCaseFilename = filename.toLowerCase();
        return ALLOWED_EXTENSIONS.stream()
                .anyMatch(lowerCaseFilename::endsWith);
    }

    private String generateUniqueFileName(String originalFilename) {
        return UUID.randomUUID() + "_" + originalFilename;
    }

    private void uploadToS3(MultipartFile file, String fileName) {
        try {
            PutObjectRequest request = createPutObjectRequest(fileName, file.getContentType());
            s3Client.putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        } catch (IOException e) {
            throw new RuntimeException("S3 업로드 실패", e);
        }
    }

    private PutObjectRequest createPutObjectRequest(String fileName, String contentType) {
        return PutObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .contentType(Optional.ofNullable(contentType).orElse(DEFAULT_CONTENT_TYPE))
                .build();
    }

    private String generateS3Url(String fileName) {
        return String.format("https://s3.%s.amazonaws.com/%s/%s", region, bucket, fileName);
    }

    private String extractKeyFromUrl(final String imageUrl) {
        int bucketIndex = imageUrl.indexOf(bucket);
        if (bucketIndex == -1) {
            throw new IllegalArgumentException("유효하지 않은 S3 URL 형식입니다.");
        }
        int keyStartIndex = bucketIndex + bucket.length() + 1;
        return imageUrl.substring(keyStartIndex);
    }

}
