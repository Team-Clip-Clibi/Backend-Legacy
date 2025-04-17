package com.clip.global.exception;

import com.clip.api.matching.service.exception.NotExistAnyMatchingException;
import com.clip.notification.exception.NotExistNotificationException;
import com.clip.api.user.service.exception.TokenValidationException;
import com.clip.user.exception.NicknameAlreadyExistsException;
import com.clip.user.exception.PhoneNumberAlreadyExistsException;
import com.clip.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class ValidExceptionHandler {
    @ExceptionHandler(PhoneNumberAlreadyExistsException.class)
    public ResponseEntity<?> phoneNumberAlreadyExistsException(PhoneNumberAlreadyExistsException e) {
        return ResponseEntity.badRequest()
                .body(e.getMessage());
    }

    @ExceptionHandler(NicknameAlreadyExistsException.class)
    public ResponseEntity<?> nicknameAlreadyExistsException(NicknameAlreadyExistsException e) {
        return ResponseEntity.badRequest()
                .body(e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> notFoundUserException(UserNotFoundException e) {
        return ResponseEntity.badRequest()
                .body(e.getMessage());
    }

    @ExceptionHandler(TokenValidationException.class)
    public ResponseEntity<?> tokenValidationException(TokenValidationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(e.getMessage());
    }

    @ExceptionHandler(NotExistNotificationException.class)
    public ResponseEntity<?> notExistNotificationException(NotExistNotificationException e) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @ExceptionHandler(NotExistAnyMatchingException.class)
    public ResponseEntity<?> notExistAnyMatchingException(NotExistAnyMatchingException e) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}