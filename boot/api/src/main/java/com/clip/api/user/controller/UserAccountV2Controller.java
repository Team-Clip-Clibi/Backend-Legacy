package com.clip.api.user.controller;

import com.clip.api.docs.user.UserAccountV2Docs;
import com.clip.api.user.controller.dto.LoginDtoV2;
import com.clip.api.user.controller.dto.SignupDtoV2;
import com.clip.api.user.service.UserAccountFacade;
import com.clip.api.user.service.UserAccountService;
import com.clip.global.config.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserAccountV2Controller implements UserAccountV2Docs {

    private final UserAccountService userAccountService;
    private final UserAccountFacade userAccountFacade;

    @Override
    public TokenProvider.Token getReissueToken(TokenProvider.RefreshToken refreshToken) {
        return userAccountService.getReissueToken(refreshToken);
    }

    @Override
    public TokenProvider.Token createUserAccount(SignupDtoV2 request) {
        return userAccountFacade.signup(request);
    }

    @Override
    public TokenProvider.Token loginUserAccount(LoginDtoV2 request) {
        return userAccountFacade.login(request);
    }
}
