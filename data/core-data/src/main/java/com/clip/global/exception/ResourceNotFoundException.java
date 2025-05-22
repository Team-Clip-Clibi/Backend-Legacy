package com.clip.global.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resourceName, Long id) {
        super(String.format("Resource %s with id %d not found", resourceName, id));
    }

    public ResourceNotFoundException(String resourceName, String identifier) {
        super(String.format("Resource %s with identifier %s not found", resourceName, identifier));
    }
}