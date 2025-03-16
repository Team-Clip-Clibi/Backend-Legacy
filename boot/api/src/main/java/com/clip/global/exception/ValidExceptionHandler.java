package com.clip.global.exception;

import com.clip.api.user.exception.NotFoundUserException;
import com.clip.user.exception.NicknameAlreadyExistsException;
import com.clip.user.exception.PhoneNumberAlreadyExistsException;
import lombok.RequiredArgsConstructor;
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

    @ExceptionHandler(NotFoundUserException.class)
    public ResponseEntity<?> notFoundUserException(NotFoundUserException e) {
        return ResponseEntity.badRequest()
                .body(e.getMessage());
    }
}