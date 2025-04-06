package com.clip.api.user.controller;

import com.clip.api.docs.user.UserAccountDocs;
import com.clip.api.user.controller.dto.*;
import com.clip.api.user.mapper.JobMapper;
import com.clip.api.user.mapper.LanguageMapper;
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
    public UserInfoDto getUserInfo(String phoneNumber) {
        return userAccountService.getUserInfo(phoneNumber);
    }

    @Override
    public TokenProvider.AccessToken getAccessToken(TokenProvider.RefreshToken refreshToken) {
        return userAccountService.getAccessToken(refreshToken);
    }
}
