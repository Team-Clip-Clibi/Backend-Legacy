package com.clip.notice.exception;

public class BannerNotFoundException extends RuntimeException {
    public BannerNotFoundException() {
        super("배너를 찾을 수 없습니다. ID=");
    }
}
