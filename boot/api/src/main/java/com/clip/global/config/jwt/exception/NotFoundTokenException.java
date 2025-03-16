package com.clip.global.config.jwt.exception;

public class NotFoundTokenException extends RuntimeException {
    public NotFoundTokenException() {super("토큰을 헤더에서 찾을 수 없습니다.");
    }
}
