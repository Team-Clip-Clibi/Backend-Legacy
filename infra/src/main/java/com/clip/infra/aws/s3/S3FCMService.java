package com.clip.infra.aws.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class S3FCMService {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloud.aws.s3.fcm.sdk}")
    private String fcmSdkName;

    private final S3Client s3Client;

    public InputStream findFcmSdk() {
        return s3Client.getObject(GetObjectRequest.builder()
                .bucket(bucket)
                .key(fcmSdkName)
                .build()
        );
    }
}

