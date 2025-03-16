package com.clip.user.exception;

public class PhoneNumberAlreadyExistsException extends RuntimeException {
    public PhoneNumberAlreadyExistsException() {
        super("이미 사용중인 번호로 업데이트 할 수 없습니다.");
    }
}
