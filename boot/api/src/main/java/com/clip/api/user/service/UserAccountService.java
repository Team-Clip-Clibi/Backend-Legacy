package com.clip.api.user.service;

import com.clip.api.user.controller.dto.*;
import com.clip.api.user.mapper.LanguageMapper;
import com.clip.api.user.mapper.TermsAcceptanceMapper;
import com.clip.api.user.mapper.UserProfileMapper;
import com.clip.api.user.service.exception.TokenValidationException;
import com.clip.auth.entity.Token;
import com.clip.auth.service.TokenService;
import com.clip.global.config.jwt.TokenProvider;
import com.clip.global.exception.ResourceAlreadyExistException;
import com.clip.matching.service.OneThingMatchingReviewService;
import com.clip.matching.service.RandomMatchingReviewService;
import com.clip.matching.service.UserOneThingMatchingService;
import com.clip.matching.service.UserRandomMatchingService;
import com.clip.notification.service.NotificationService;
import com.clip.user.entity.JobCategory;
import com.clip.user.entity.RelationshipStatus;
import com.clip.user.entity.User;
import com.clip.user.service.JobService;
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
    private final LanguageMapper languageMapper;
    private final UserOneThingMatchingService userOneThingMatchingService;
    private final UserRandomMatchingService userRandomMatchingService;
    private final OneThingMatchingReviewService oneThingMatchingReviewService;
    private final RandomMatchingReviewService randomMatchingReviewService;
    private final NotificationService notificationService;
    private final JobService jobService;

    @Transactional
    public TokenProvider.Token signup(SignupDto request) {
        Optional<User> optUser = userService.findOptUser(request.getSocialId(), request.getPlatform());

        if (optUser.isPresent()) {
            TokenProvider.Token token = tokenProvider.generateToken(optUser.get().getId(), LocalDateTime.now());
            tokenService.updateRefreshToken(optUser.get(), token.refreshToken());
            userService.updateDeviceInfo(optUser.get().getId(), request.getDeviceType(), request.getOsVersion(), request.getFirebaseToken(), request.getIsAllowNotify());
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

        User user = userService.findUser(request.getSocialId(), request.getPlatform());

        TokenProvider.Token token = tokenProvider.generateToken(user.getId(), LocalDateTime.now());
        tokenService.updateRefreshToken(user, token.refreshToken());
        userService.updateDeviceInfo(user.getId(), request.getDeviceType(), request.getOsVersion(), request.getFirebaseToken(), request.getIsAllowNotify());
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

    public UserInfoDto getUserInfo(long userId, String phoneNumber) {
        User user = userService.findUserExcludeOwner(userId, phoneNumber);
        return UserInfoDto.builder()
                .createdAt(user.getCreatedAt())
                .userName(user.getUsername())
                .platform(user.getPlatform())
                .build();
    }

    public void checkNicknameAvailable(String nickname) {
        if (userService.isExistNickname(nickname)) {
            throw new ResourceAlreadyExistException("nickname", nickname);
        }
    }

    public RetrieveUserProfileInfo getUserProfileInfo(long userId) {
        User user = userService.findUser(userId);
        return userProfileMapper.toRetrieveUserProfileInfo(user);
    }

    @Transactional
    public void updateFCMToken(long userId, String fcmToken) {
        userService.findUser(userId).updateFirebaseToken(fcmToken);
    }

    @Transactional
    public void updateNotifyAllow(long userId, boolean isAllowNotify) {
        userService.findUser(userId).updateIsAllowNotify(isAllowNotify);
    }

    @Transactional
    public void updateJob(long userId, JobCategory jobCategory) {
        userService.findUser(userId)
                .updateJob(jobService.findJob(jobCategory));
    }

    @Transactional
    public void updateRelationship(long userId, RelationshipStatus relationshipStatus, boolean isSameRelationshipConsidered) {
        userService.findUser(userId)
                .updateRelationshipAndConsidered(relationshipStatus, isSameRelationshipConsidered);
    }

    @Transactional
    public void updateDietaryOption(long userId, String dietaryOption) {
        userService.findUser(userId)
                .updateDietaryOption(dietaryOption);
    }

    @Transactional
    public void updateLanguage(long userId, LanguageDto languageDto) {
        userService.findUser(userId)
                .updateLanguage(languageMapper.toStringLanguage(languageDto.getLanguage()));
    }

    @Transactional(readOnly = true)
    public JobDto getJob(long userId) {
        return JobDto.builder()
                .job(userService.findUser(userId).getJob().getJobCategory())
                .build();
    }

    public RelationshipDto getReplationship(long userId) {
        User user = userService.findUser(userId);
        return RelationshipDto.builder()
                .relationshipStatus(user.getRelationshipStatus())
                .isSameRelationshipConsidered(user.getIsSameRelationshipConsidered())
                .build();
    }

    public DietaryDto getDietaryOption(long userId) {
        return DietaryDto.builder()
                .dietaryOption(userService.findUser(userId).getDietaryOption())
                .build();
    }

    public LanguageDto getLanguage(long userId) {
        return languageMapper.toLanguageDto(userService.findUser(userId).getLanguage());
    }

    public TokenProvider.AccessToken getAccessToken(TokenProvider.RefreshToken refreshToken) {
        if (!tokenProvider.isValidRefreshToken(refreshToken.refreshToken())) {
            throw new TokenValidationException();
        }
        User user = tokenService.findRefreshToken(refreshToken.refreshToken());
        return new TokenProvider.AccessToken(
                tokenProvider.generateAccessToken(user.getId(), LocalDateTime.now())
        );
    }

    @Transactional
    public void deleteUser(String authorizationHeader) {
        String refreshToken = authorizationHeader.replace("Bearer ", "");
        if (!tokenProvider.isValidRefreshToken(refreshToken)) {
            throw new TokenValidationException();
        }
        long userId = Long.parseLong(tokenProvider.extractUserId(refreshToken));
        if (isMyMatchingExist(userId)) {
            throw new IllegalStateException("매칭이 존재하는 유저는 탈퇴할 수 없습니다.");
        }
        User user = userService.findUser(userId);
        notificationService.deleteNotification(userId);
        oneThingMatchingReviewService.deleteOneThingMatchingReview(userId);
        randomMatchingReviewService.deleteRandomMatchingReview(userId);
        userOneThingMatchingService.deleteOneThingMatching(userId);
        userRandomMatchingService.deleteRandomMatching(userId);
        termsAcceptanceService.deleteTermsAcceptance(userId);
        userService.deleteUser(user);
    }

    public boolean isMyMatchingExist(long userId) {
        boolean isOneThingMatchingExist = userOneThingMatchingService.isOneThingMatchingExist(userId);
        boolean isRandomMatchingExist = userRandomMatchingService.isRandomMatchingExist(userId);
        return isOneThingMatchingExist || isRandomMatchingExist;
    }
}
