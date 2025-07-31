package com.clip.global.exception;

public class NoContentAvailableException extends RuntimeException {

    public NoContentAvailableException(String contentName, String identifier) {
        super(String.format("Content %s with identifier %s not exist", contentName, identifier));
    }
    public NoContentAvailableException(String contentName, Long id) {
        super(String.format("Content %s with id %d not exist", contentName, id));
    }
}
