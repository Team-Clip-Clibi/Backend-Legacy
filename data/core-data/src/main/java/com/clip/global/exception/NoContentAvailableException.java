package com.clip.global.exception;

public class NoContentAvailableException extends RuntimeException {

    public NoContentAvailableException(String contentName, String identifier) {
        super(String.format("Content %s with identifier %s already exist", contentName, identifier));
    }
    public NoContentAvailableException(String contentName, Long id) {
        super(String.format("Content %s with id %d already exist", contentName, id));
    }
}
