package com.clip.global.exception;

public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String message) {
        super(String.format("This request cannot be processed due to invalid parameters: %s", message));
    }
}
