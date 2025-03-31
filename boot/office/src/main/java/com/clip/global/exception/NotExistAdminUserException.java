package com.clip.global.exception;

public class NotExistAdminUserException extends RuntimeException {
    public NotExistAdminUserException() {
        super("username 파라미터가 비어있습니다.");
    }
}
