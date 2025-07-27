package com.clip.api.user.controller.dto;

import com.clip.user.entity.DeviceType;
import com.clip.user.entity.Platform;
import com.clip.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class SignupDtoV2 {
    @NotNull
    private Boolean servicePermission;
    @NotNull
    private Boolean privatePermission;
    @NotNull
    private Boolean marketingPermission;
    @NotBlank
    private String accessToken;
    @NotNull
    private Platform platform;
    @NotNull
    private DeviceType deviceType;
    @NotBlank
    private String osVersion;
    private String firebaseToken;
    @NotNull
    private Boolean isAllowNotify;

    @Builder
    public SignupDtoV2(Boolean servicePermission, Boolean privatePermission, Boolean marketingPermission, String accessToken, Platform platform, DeviceType deviceType, String osVersion, String firebaseToken, Boolean isAllowNotify) {
        this.servicePermission = servicePermission;
        this.privatePermission = privatePermission;
        this.marketingPermission = marketingPermission;
        this.accessToken = accessToken;
        this.platform = platform;
        this.deviceType = deviceType;
        this.osVersion = osVersion;
        this.firebaseToken = firebaseToken;
        this.isAllowNotify = isAllowNotify;
    }

    public User toUser() {
        return User.builder()
                .socialId(this.getAccessToken())
                .platform(this.getPlatform())
                .deviceType(this.getDeviceType())
                .osVersion(this.getOsVersion())
                .firebaseToken(this.getFirebaseToken())
                .isAllowNotify(this.getIsAllowNotify())
                .build();
    }
}
