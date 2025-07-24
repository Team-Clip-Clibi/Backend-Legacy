package com.clip.infra.aws.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class S3ImgService {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final S3Presigner s3Presigner;
    private final S3Client s3Client;

    private static final Duration EXPIRY_TIME = Duration.ofHours(24);

    public String generateGetPresignedUrl(String imgPathPrefix, String imgName) {
        return s3Presigner.presignGetObject(
                        GetObjectPresignRequest.builder()
                                .getObjectRequest(GetObjectRequest.builder()
                                        .bucket(bucket)
                                        .key(imgPathPrefix + imgName)
                                        .build())
                                .signatureDuration(EXPIRY_TIME)
                                .build())
                .url()
                .toString();
    }

    public String generatePutPresignedUrl(String filePath, String imgName) {
        return s3Presigner.presignPutObject(PutObjectPresignRequest.builder()
                        .putObjectRequest(PutObjectRequest.builder()
                                .bucket(bucket)
                                .key(filePath + imgName)
                                .build())
                        .signatureDuration(EXPIRY_TIME)
                        .build())
                .url().toString();
    }

    public boolean isImgSaved(String filePath, String imgName) {
        try {
            s3Client.headObject(HeadObjectRequest.builder()
                    .bucket(bucket)
                    .key(filePath + imgName)
                    .build());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void deleteImg(String filePath, String imgName) {
        ObjectIdentifier deleteObject = ObjectIdentifier.builder()
                .key(filePath + imgName)
                .build();

        Delete del = Delete.builder()
                .objects(deleteObject)
                .build();

        DeleteObjectsRequest deleteRequest = DeleteObjectsRequest.builder()
                .bucket(bucket)
                .delete(del)
                .build();

        s3Client.deleteObjects(deleteRequest);
    }
}
