package com.clip.api.user.exception;

public class NotFoundUserException extends RuntimeException {
    public NotFoundUserException() {
        super("가입되지 않은 유저입니다.");
    }
}
