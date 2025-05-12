package com.clip.global.config.feign;

import feign.RequestInterceptor;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;

@Configuration
public class FeignConfig {

    @Value("${toss.secret-key}")
    private String tossSecretKey;

    @Bean
    public RequestInterceptor authorizationHeader() {
        return requestTemplate -> requestTemplate.header("Authorization", getEncodedSecretKey());
    }

    private String getEncodedSecretKey() {
        String key = tossSecretKey + ":";
        return "Basic " + new String(Base64.encode(key.getBytes(StandardCharsets.UTF_8)));
    }
}
