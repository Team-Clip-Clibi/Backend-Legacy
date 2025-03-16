package com.clip.user.exception;

public class NicknameAlreadyExistsException extends RuntimeException {
    public NicknameAlreadyExistsException() {
        super("이미 사용중인 닉네임으로 업데이트 할 수 없습니다.");
    }
}
