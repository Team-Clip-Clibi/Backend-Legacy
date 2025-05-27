package com.clip.global.exception;

import com.clip.api.matching.service.exception.MatchingFailedException;
import com.clip.api.user.service.exception.TokenValidationException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class ValidExceptionHandler {
    @ExceptionHandler(ResourceAlreadyExistException.class)
    public ResponseEntity<?> resourceAlreadyExistsException(ResourceAlreadyExistException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(TokenValidationException.class)
    public ResponseEntity<?> tokenValidationException(TokenValidationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(e.getMessage());
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<?> invalidRequestException(TokenValidationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(NoContentAvailableException.class)
    public ResponseEntity<?> noContentAvailableException(NoContentAvailableException e) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<?> FeignException(FeignException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(MatchingFailedException.class)
    public ResponseEntity<?> matchingFailedException(MatchingFailedException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}