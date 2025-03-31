package com.clip.admin.exception;

public class NotFoundAdminUserException extends RuntimeException {
    public NotFoundAdminUserException() {
        super("존재하지 않은 아이디입니다.");
    }
}
