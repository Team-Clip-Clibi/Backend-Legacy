package com.clip.api.user.controller.dto;

import com.clip.user.entity.DeviceType;
import com.clip.user.entity.Platform;
import com.clip.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupDto {
    private boolean servicePermission;
    private boolean privatePermission;
    private boolean marketingPermission;
    private String socialId;
    private Platform platform;
    private DeviceType deviceType;
    private String osVersion;
    private String firebaseToken;

    @Builder
    public SignupDto(boolean servicePermission, boolean privatePermission, boolean marketingPermission, String socialId, Platform platform, DeviceType deviceType, String osVersion, String firebaseToken) {
        this.servicePermission = servicePermission;
        this.privatePermission = privatePermission;
        this.marketingPermission = marketingPermission;
        this.socialId = socialId;
        this.platform = platform;
        this.deviceType = deviceType;
        this.osVersion = osVersion;
        this.firebaseToken = firebaseToken;
    }

    public User toUser() {
        return User.builder()
                .socialId(this.getSocialId())
                .platform(this.getPlatform())
                .deviceType(this.getDeviceType())
                .osVersion(this.getOsVersion())
                .firebaseToken(this.getFirebaseToken())
                .build();
    }
}
