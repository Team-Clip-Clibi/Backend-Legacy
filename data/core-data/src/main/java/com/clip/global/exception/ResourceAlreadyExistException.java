package com.clip.global.exception;

public class ResourceAlreadyExistException extends RuntimeException {
    public ResourceAlreadyExistException(String resourceName, String identifier) {
        super(String.format("Resource %s with identifier %s already exist", resourceName, identifier));
    }
    public ResourceAlreadyExistException(String resourceName, Long id) {
        super(String.format("Resource %s with id %d already exist", resourceName, id));
    }
}
