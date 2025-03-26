package com.clip.api.admin.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminLoginDto {
    private String username;
    private String password;
}
