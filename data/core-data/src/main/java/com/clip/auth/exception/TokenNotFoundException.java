package com.clip.auth.exception;

public class TokenNotFoundException extends RuntimeException {
    public TokenNotFoundException() {
        super("Refresh Token을 찾을 수 없습니다.");
    }
}
