package com.clip.api.user.controller.dto;

import com.clip.user.entity.DeviceType;
import com.clip.user.entity.Platform;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginDtoV2 {
    @NotNull
    private String accessToken;
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
    public LoginDtoV2(String socialId, Platform platform, DeviceType deviceType, String osVersion, String firebaseToken, Boolean isAllowNotify) {
        this.accessToken = socialId;
        this.platform = platform;
        this.deviceType = deviceType;
        this.osVersion = osVersion;
        this.firebaseToken = firebaseToken;
        this.isAllowNotify = isAllowNotify;
    }
}
