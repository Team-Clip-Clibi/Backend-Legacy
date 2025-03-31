package com.clip.office.admin.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class NotExistAdminUserException extends UsernameNotFoundException {
    public NotExistAdminUserException() {
        super("username 파라미터가 비어있습니다.");
    }
}
