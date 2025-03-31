package com.clip.api.user.controller.dto;

import com.clip.user.entity.DeviceType;
import com.clip.user.entity.Platform;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginDto {
    @NotNull
    private String socialId;
    @NotNull
    private Platform platform;
    @NotNull
    private DeviceType deviceType;
    @NotNull
    private String osVersion;
    private String firebaseToken;
    @NotNull
    private Boolean isAllowNotify;

    @Builder
    public LoginDto(String socialId, Platform platform, DeviceType deviceType, String osVersion, String firebaseToken, Boolean isAllowNotify) {
        this.socialId = socialId;
        this.platform = platform;
        this.deviceType = deviceType;
        this.osVersion = osVersion;
        this.firebaseToken = firebaseToken;
        this.isAllowNotify = isAllowNotify;
    }
}
