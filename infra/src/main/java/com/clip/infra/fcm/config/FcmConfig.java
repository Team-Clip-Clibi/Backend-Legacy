package com.clip.infra.fcm.config;

import com.clip.infra.aws.s3.S3FCMService;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class FcmConfig {
    private final S3FCMService s3FCMService;

    @PostConstruct
    public void initialize() {
        if (!FirebaseApp.getApps().isEmpty()) {
            return;
        }

        try (InputStream sdk = s3FCMService.findFcmSdk()) {
            FirebaseApp.initializeApp(fcmOptions(sdk));
            log.info("FirebaseApp initialized successfully");
        } catch (IOException e) {
            log.error("Firebase 초기화 실패: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private FirebaseOptions fcmOptions(InputStream sdk) throws IOException {
        return FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(sdk))
                .build();
    }
}
