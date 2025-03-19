package com.clip.api.user.controller.dto;

import com.clip.user.entity.Platform;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RetrieveUserProfileInfo {
    private String username;
    private Platform platform;
    private String phoneNumber;
    private String nickname;

    @Builder
    public RetrieveUserProfileInfo(String username, Platform platform, String phoneNumber, String nickname) {
        this.username = username;
        this.platform = platform;
        this.phoneNumber = phoneNumber;
        this.nickname = nickname;
    }
}
