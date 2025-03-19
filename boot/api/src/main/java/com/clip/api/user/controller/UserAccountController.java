package com.clip.api.user.controller;

import com.clip.api.docs.user.UserAccountDocs;
import com.clip.api.user.controller.dto.*;
import com.clip.api.user.service.UserAccountService;
import com.clip.global.config.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class UserAccountController implements UserAccountDocs {
    private final UserAccountService userAccountService;

    @Override
    public TokenProvider.Token createUserAccount(SignupDto request) {
        return userAccountService.signup(request);
    }

    @Override
    public TokenProvider.Token loginUserAccount(LoginDto request) {
        return userAccountService.login(request);
    }

    @Override
    public void updatePhoneNumber(UpdatePhoneNumberDto updatePhoneNumberDto,
                                                  UserDetails userDetails) {
        userAccountService.updatePhoneNumber(Long.parseLong(userDetails.getUsername()), updatePhoneNumberDto.getPhoneNumber());
    }

    @Override
    public void updateName(UpdateNameDto updateNameDto,
                                           UserDetails userDetails) {
        userAccountService.updateName(Long.parseLong(userDetails.getUsername()), updateNameDto.getUserName());
    }

    @Override
    public void updateNickname(UpdateNicknameDto updateNicknameDto,
                                               UserDetails userDetails) {
        userAccountService.updateNickname(Long.parseLong(userDetails.getUsername()), updateNicknameDto.getNickname());
    }

    @Override
    public void checkNicknameAvailable(UpdateNicknameDto updateNicknameDto) {
        userAccountService.checkNicknameAvailable(updateNicknameDto.getNickname());
    }

    @Override
    public void updateUserDetailInfo(UpdateUserDetailInfoDto updateUserDetailInfoDto, UserDetails userDetails) {
        userAccountService.updateUserDetailInfo(Long.parseLong(userDetails.getUsername()), updateUserDetailInfoDto);
    }

    @Override
    public UserInfoDto getUserInfo(String phoneNumber) {
        return userAccountService.getUserInfo(phoneNumber);
    }

    @Override
    public RetrieveUserProfileInfo getProfileInfo(UserDetails userDetails) {
        return userAccountService.getUserProfileInfo(Long.parseLong(userDetails.getUsername()));
    }

    @Override
    public void updateFCMToken(UpdateFCMDto updateFCMDto, UserDetails userDetails) {
        userAccountService.updateFCMToken(Long.parseLong(userDetails.getUsername()), updateFCMDto.getFcmToken());
    }

    @Override
    public void updateNotifyAllow(UpdateNotifyAllowDto updateNotifyAllowDto, UserDetails userDetails) {
        userAccountService.updateNotifyAllow(Long.parseLong(userDetails.getUsername()), updateNotifyAllowDto.isAllowNotify());
    }
}
