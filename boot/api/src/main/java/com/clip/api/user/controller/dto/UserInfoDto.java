package com.clip.api.user.controller.dto;

import com.clip.user.entity.Platform;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class UserInfoDto {
    private String userName;
    private Platform platform;
    private LocalDateTime createdAt;

    @Builder
    public UserInfoDto(String userName, Platform platform, LocalDateTime createdAt) {
        this.userName = userName;
        this.platform = platform;
        this.createdAt = createdAt;
    }
}
