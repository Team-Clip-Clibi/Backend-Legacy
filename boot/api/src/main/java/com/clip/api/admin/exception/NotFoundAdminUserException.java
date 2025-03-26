package com.clip.api.admin.exception;

public class NotFoundAdminUserException extends RuntimeException {
    public NotFoundAdminUserException() {
        super("admin를 찾을수 없습니다.");
    }
}
