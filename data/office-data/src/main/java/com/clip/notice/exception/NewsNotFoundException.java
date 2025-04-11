package com.clip.notice.exception;

public class NewsNotFoundException extends RuntimeException {
    public NewsNotFoundException() {
        super("News를 찾을 수 없습니다.");
    }
}
