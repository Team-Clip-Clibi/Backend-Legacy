package com.clip.api.user.controller;

import com.clip.api.docs.user.UserInfoDocs;
import com.clip.api.user.controller.dto.*;
import com.clip.api.user.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserInfoController implements UserInfoDocs {
    private final UserAccountService userAccountService;

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

    @Override
    public void updateJob(JobDto jobDto, UserDetails userDetails) {
        userAccountService.updateJob(Long.parseLong(userDetails.getUsername()), jobDto);
    }

    @Override
    public JobDto getJob(UserDetails userDetails) {
        return userAccountService.getJob(Long.parseLong(userDetails.getUsername()));
    }

    @Override
    public void updateRelationship(RelationshipDto relationshipDto, UserDetails userDetails) {
        userAccountService.updateRelationship(Long.parseLong(userDetails.getUsername()), relationshipDto.getRelationshipStatus(), relationshipDto.getIsSameRelationshipConsidered());
    }

    @Override
    public RelationshipDto getRelationship(UserDetails userDetails) {
        return userAccountService.getReplationship(Long.parseLong(userDetails.getUsername()));
    }

    @Override
    public void updateDietaryOption(DietaryDto dietaryDto, UserDetails userDetails) {
        userAccountService.updateDietaryOption(Long.parseLong(userDetails.getUsername()), dietaryDto.getDietaryOption());
    }

    @Override
    public DietaryDto getDietaryOption(UserDetails userDetails) {
        return userAccountService.getDietaryOption(Long.parseLong(userDetails.getUsername()));
    }

    @Override
    public void updateLanguage(LanguageDto languageDto, UserDetails userDetails) {
        userAccountService.updateLanguage(Long.parseLong(userDetails.getUsername()), languageDto);
    }

    @Override
    public LanguageDto getLanguage(UserDetails userDetails) {
        return userAccountService.getLanguage(Long.parseLong(userDetails.getUsername()));
    }
}
