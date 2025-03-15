package com.clip.global.config.jwt;

import com.clip.global.config.jwt.exception.InvalidTokenException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class TokenProvider {
    private final String ISSUER = "clip";
    private final ZoneId ZONE_ID = ZoneId.of("Asia/Seoul");
    private final JWTProperties jwtProperties;

    public Token generateToken(long userId, LocalDateTime currentTime) {
        return new Token(generateAccessToken(userId, currentTime), generateRefreshToken(userId, currentTime));
    }

    public String generateAccessToken(long userId, LocalDateTime currentDateTime) {
        return generateToken(new CustomClaims(userId,TokenType.ACCESS_TOKEN), currentDateTime, currentDateTime.plusDays(jwtProperties.getAccessTokenExpirationPeriodDay()));
    }

    public String generateRefreshToken(long userId, LocalDateTime currentDateTime) {
        return generateToken(new CustomClaims(userId,TokenType.REFRESH_TOKEN), currentDateTime, currentDateTime.plusMonths(jwtProperties.getRefreshTokenExpirationPeriodMonth()));
    }

    public String extractUserId(String token) {
        return initParser()
                .parseSignedClaims(token)
                .getPayload()
                .get("userId", String.class);
    }

    public void validateToken(String token) {
        JwtParser jwtParser = initParser();
        try {
            jwtParser.parse(token);
        } catch (Exception e) {
            throw new InvalidTokenException();
        }
    }


    private JwtParser initParser() {
        return Jwts.parser()
                .json(new JacksonDeserializer<>())
                .verifyWith(getSigningKey())
                .requireIssuer(ISSUER)
                .build();
    }

    private String generateToken(CustomClaims customClaims, LocalDateTime currentDateTime, LocalDateTime expiresAt) {
        return Jwts.builder()
                .issuer(ISSUER)
                .claims(customClaims.getClaims())
                .issuedAt(Date.from(currentDateTime.atZone(ZONE_ID).toInstant()))
                .expiration(Date.from(expiresAt.atZone(ZONE_ID).toInstant()))
                .signWith(getSigningKey())
                .compact();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
    }

    public record Token(String accessToken, String refreshToken) {
    }
}
