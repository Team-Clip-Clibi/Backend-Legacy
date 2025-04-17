package com.clip.api.matching.service.exception;

public class NotExistAnyMatchingException extends RuntimeException {
    public NotExistAnyMatchingException() {
        super("어떤 매칭 존재하지 않습니다.");
    }
}
