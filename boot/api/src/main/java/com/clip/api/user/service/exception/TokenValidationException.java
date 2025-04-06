package com.clip.api.user.service.exception;

public class TokenValidationException extends RuntimeException {
    public TokenValidationException() {
        super("토큰 검증에 실패했습니다.");
    }
}
