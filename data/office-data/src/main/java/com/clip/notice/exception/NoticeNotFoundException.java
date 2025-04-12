package com.clip.notice.exception;

public class NoticeNotFoundException extends RuntimeException {
    public NoticeNotFoundException() {
        super("Notice 를 찾을 수 없습니다.");
    }
}
