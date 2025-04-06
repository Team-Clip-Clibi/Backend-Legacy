package com.clip.matching.exception;

public class MatchingNotFoundException extends RuntimeException {
    public MatchingNotFoundException() {
        super("매칭 정보를 찾을 수 없습니다.");
    }
}
