package com.clip.api.user.controller.dto;

import com.clip.user.entity.DeviceType;
import com.clip.user.entity.Platform;
import com.clip.user.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class SignupDto {
    @NotNull
    private Boolean servicePermission;
    @NotNull
    private Boolean privatePermission;
    @NotNull
    private Boolean marketingPermission;
    @NotBlank
    private String socialId;
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
    public SignupDto(Boolean servicePermission, Boolean privatePermission, Boolean marketingPermission, String socialId, Platform platform, DeviceType deviceType, String osVersion, String firebaseToken, Boolean isAllowNotify) {
        this.servicePermission = servicePermission;
        this.privatePermission = privatePermission;
        this.marketingPermission = marketingPermission;
        this.socialId = socialId;
        this.platform = platform;
        this.deviceType = deviceType;
        this.osVersion = osVersion;
        this.firebaseToken = firebaseToken;
        this.isAllowNotify = isAllowNotify;
    }

    public User toUser() {
        return User.builder()
                .socialId(this.getSocialId())
                .platform(this.getPlatform())
                .deviceType(this.getDeviceType())
                .osVersion(this.getOsVersion())
                .firebaseToken(this.getFirebaseToken())
                .isAllowNotify(this.getIsAllowNotify())
                .build();
    }
}
