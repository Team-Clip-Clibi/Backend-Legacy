package com.clip.matching.exception;

public class NotExistMatchingException extends RuntimeException {
    public NotExistMatchingException() {
        super("조회할 매칭이 존재하지 않습니다.");
    }
}
