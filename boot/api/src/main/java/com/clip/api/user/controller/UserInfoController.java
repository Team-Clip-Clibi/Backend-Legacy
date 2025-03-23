package com.clip.api.user.controller;

import com.clip.api.docs.user.UserInfoDocs;
import com.clip.api.user.controller.dto.*;
import com.clip.api.user.mapper.JobMapper;
import com.clip.api.user.mapper.LanguageMapper;
import com.clip.api.user.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserInfoController implements UserInfoDocs {
    private final UserAccountService userAccountService;
    private final JobMapper jobMapper;
    private final LanguageMapper languageMapper;

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
    public void updateJob(UpdateJobDto updateJobDto, UserDetails userDetails) {
        userAccountService.updateJob(Long.parseLong(userDetails.getUsername()), jobMapper.toStringJobList(updateJobDto.getJobList()));
    }

    @Override
    public void updateRelationship(UpdateRelationshipDto updateRelationshipDto, UserDetails userDetails) {
        userAccountService.updateRelationship(Long.parseLong(userDetails.getUsername()), updateRelationshipDto.getRelationshipStatus(), updateRelationshipDto.isSameRelationshipConsidered());
    }

    @Override
    public void updateDietaryOption(UpdateDietaryDto updateDietaryDto, UserDetails userDetails) {
        userAccountService.updateDietaryOption(Long.parseLong(userDetails.getUsername()), updateDietaryDto.getDietaryOption());
    }

    @Override
    public void updateLanguage(UpdateLanguageDto updateLanguageDto, UserDetails userDetails) {
        userAccountService.updateLanguage(Long.parseLong(userDetails.getUsername()), languageMapper.toStringLanguage(updateLanguageDto.getLanguage()));
    }
}
