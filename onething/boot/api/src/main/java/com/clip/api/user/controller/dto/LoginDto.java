package com.clip.api.user.controller.dto;

import com.clip.user.entity.DeviceType;
import com.clip.user.entity.Platform;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginDto {
    private String socialId;
    private Platform platform;
    private DeviceType deviceType;
    private String osVersion;
    private String firebaseToken;

    @Builder
    public LoginDto(String socialId, Platform platform, DeviceType deviceType, String osVersion, String firebaseToken) {
        this.socialId = socialId;
        this.platform = platform;
        this.deviceType = deviceType;
        this.osVersion = osVersion;
        this.firebaseToken = firebaseToken;
    }
}
