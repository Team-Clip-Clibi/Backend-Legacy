package com.clip.api.user.service;

import com.clip.api.user.controller.dto.*;
import com.clip.api.user.exception.NotFoundUserException;
import com.clip.api.user.mapper.TermsAcceptanceMapper;
import com.clip.api.user.mapper.UserProfileMapper;
import com.clip.auth.entity.Token;
import com.clip.auth.service.TokenService;
import com.clip.global.config.jwt.TokenProvider;
import com.clip.user.entity.User;
import com.clip.user.exception.NicknameAlreadyExistsException;
import com.clip.user.service.TermsAcceptanceService;
import com.clip.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserAccountService {
    private final TokenProvider tokenProvider;
    private final UserService userService;
    private final TermsAcceptanceService termsAcceptanceService;
    private final TokenService tokenService;
    private final TermsAcceptanceMapper termsAcceptanceMapper;
    private final UserProfileMapper userProfileMapper;

    @Transactional
    public TokenProvider.Token signup(SignupDto request) {
        Optional<User> optUser = userService.findOptUser(request.getSocialId(), request.getPlatform());

        if (optUser.isPresent()) {
            TokenProvider.Token token = tokenProvider.generateToken(optUser.get().getId(), LocalDateTime.now());
            tokenService.updateRefreshToken(optUser.get(), token.refreshToken());
            userService.updateDeviceInfo(optUser.get().getId(), request.getDeviceType(), request.getOsVersion(), request.getFirebaseToken());
            return token;
        }

        User user = userService.save(request.toUser());
        termsAcceptanceService.save(termsAcceptanceMapper.toTermsAcceptance(request, user));

        TokenProvider.Token token = tokenProvider.generateToken(user.getId(), LocalDateTime.now());
        tokenService.save(Token.builder().refreshToken(token.refreshToken()).user(user).build());
        return token;
    }

    @Transactional
    public TokenProvider.Token login(LoginDto request) {
        Optional<User> optUser = userService.findOptUser(request.getSocialId(), request.getPlatform());

        if (optUser.isEmpty()) {
            throw new NotFoundUserException();
        }

        TokenProvider.Token token = tokenProvider.generateToken(optUser.get().getId(), LocalDateTime.now());
        tokenService.updateRefreshToken(optUser.get(), token.refreshToken());
        userService.updateDeviceInfo(optUser.get().getId(), request.getDeviceType(), request.getOsVersion(), request.getFirebaseToken());
        return token;
    }

    public void updatePhoneNumber(long userId, String phoneNumber) {
        userService.updatePhoneNumber(userId, phoneNumber);
    }

    public void updateName(long userId, String userName) {
        userService.updateUserName(userId, userName);
    }

    public void updateNickname(long userId, String nickname) {
        userService.updateNickname(userId, nickname);
    }

    public void updateUserDetailInfo(long userId, UpdateUserDetailInfoDto request) {
        userService.updateUserDetailInfo(
                userId,
                request.getGender(),
                request.getBirth(),
                request.getCity(),
                request.getCounty()
        );
    }

    public UserInfoDto getUserInfo(String phoneNumber) {
        User user = userService.findUser(phoneNumber);
        return UserInfoDto.builder()
                .createdAt(user.getCreatedAt())
                .userName(user.getUsername())
                .platform(user.getPlatform())
                .build();
    }

    public void checkNicknameAvailable(String nickname) {
        if (userService.isExistNickname(nickname)) {
            throw new NicknameAlreadyExistsException();
        }
    }

    public RetrieveUserProfileInfo getUserProfileInfo(long userId) {
        User user = userService.findUser(userId);
        return userProfileMapper.toRetrieveUserProfileInfo(user);
    }

    @Transactional
    public void updateFCMToken(long userId, String fcmToken) {
        userService.findUser(userId).setFirebaseToken(fcmToken);
    }

    @Transactional
    public void updateNotifyAllow(long userId, boolean isAllowNotify) {
        userService.findUser(userId).setAllowNotify(isAllowNotify);
    }
}
