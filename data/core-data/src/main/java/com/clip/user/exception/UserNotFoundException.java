package com.clip.user.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("유저 정보를 찾을 수 없습니다.");
    }
}
