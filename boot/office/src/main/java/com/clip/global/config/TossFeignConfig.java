package com.clip.global.config;

import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

@Slf4j
public class TossFeignConfig {

    @Value("${toss.secret-key}")
    private String tossSecretKey;

    @Bean
    public RequestInterceptor authorizationHeader() {
        return requestTemplate -> requestTemplate.header("Authorization", getEncodedSecretKey());
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return (methodKey, response) -> {
            String errorMessage = Objects.isNull(response.body()) ? "No response body" : response.body().toString();
            log.error("Feign client error: Method Key - {}, Status Code - {}, Response Body - {}", methodKey, response.status(), errorMessage);
            return new ErrorDecoder.Default().decode(methodKey, response);
        };
    }

    private String getEncodedSecretKey() {
        String key = tossSecretKey + ":";
        return "Basic " + Base64.getEncoder().encodeToString(key.getBytes(StandardCharsets.UTF_8));
    }

}
