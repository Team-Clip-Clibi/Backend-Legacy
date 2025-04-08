package com.clip.api.notification.service.exception;

public class NotExistNotificationException extends RuntimeException {
    public NotExistNotificationException() {
        super("조회할 알림이 존재하지 않습니다.");
    }
}
