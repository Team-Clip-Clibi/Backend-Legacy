package com.clip.infra.fcm.exception;

public class FcmServerException extends RuntimeException {
    public FcmServerException() {
        super("Firebase Cloud Messaging 서버 통신 오류가 발생했습니다");
    }
}
