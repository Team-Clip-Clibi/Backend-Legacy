package com.clip.api.user.service;

import com.clip.api.user.controller.dto.LoginDto;
import com.clip.api.user.controller.dto.LoginDtoV2;
import com.clip.api.user.controller.dto.SignupDto;
import com.clip.api.user.controller.dto.SignupDtoV2;
import com.clip.api.user.feign.KakaoLoginFeign;
import com.clip.global.config.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAccountFacade {
    private final KakaoLoginFeign kakaoLoginFeign;
    private final UserAccountService userAccountService;

    public TokenProvider.Token signup(SignupDtoV2 request) {

        String socialId = switch (request.getPlatform()) {
            case KAKAO -> kakaoLoginFeign.getUserInfo("Bearer " + request.getAccessToken()).id().toString();
            case APPLE -> null;
        };

        return userAccountService.signup(SignupDto.builder()
                .deviceType(request.getDeviceType())
                .firebaseToken(request.getFirebaseToken())
                .isAllowNotify(request.getIsAllowNotify())
                .marketingPermission(request.getMarketingPermission())
                .osVersion(request.getOsVersion())
                .platform(request.getPlatform())
                .privatePermission(request.getPrivatePermission())
                .servicePermission(request.getServicePermission())
                .socialId(socialId)
                .build());
    }

    public TokenProvider.Token login(LoginDtoV2 request) {
        String socialId = switch (request.getPlatform()) {
            case KAKAO -> kakaoLoginFeign.getUserInfo("Bearer " + request.getAccessToken()).id().toString();
            case APPLE -> null;
        };

        return userAccountService.login(LoginDto.builder()
                .deviceType(request.getDeviceType())
                .firebaseToken(request.getFirebaseToken())
                .isAllowNotify(request.getIsAllowNotify())
                .osVersion(request.getOsVersion())
                .socialId(socialId)
                .platform(request.getPlatform())
                .build());
    }
}
