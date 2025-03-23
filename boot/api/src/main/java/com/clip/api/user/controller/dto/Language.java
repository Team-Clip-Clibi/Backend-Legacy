package com.clip.api.user.controller.dto;

public enum Language {
    KOREAN("한국어"),
    ENGLISH("English"),
    BOTH("한국어, English 모두 가능");

    private String value;
    Language(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
