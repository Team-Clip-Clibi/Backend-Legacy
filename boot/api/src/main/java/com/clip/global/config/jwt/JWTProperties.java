package com.clip.global.config.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "jwt")
public class JWTProperties {
    private final String secretKey;
    private final int accessTokenExpirationPeriodDay;
    private final int refreshTokenExpirationPeriodMonth;
}
