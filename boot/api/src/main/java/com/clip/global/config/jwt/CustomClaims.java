package com.clip.global.config.jwt;

import lombok.Getter;

import java.util.Map;

@Getter
public class CustomClaims {
    private final Map<String, String> claims;

    public CustomClaims(long userId, TokenType tokenType) {
        this.claims = Map.of("userId",String.valueOf(userId),"tokenType",tokenType.toString());
    }
}
