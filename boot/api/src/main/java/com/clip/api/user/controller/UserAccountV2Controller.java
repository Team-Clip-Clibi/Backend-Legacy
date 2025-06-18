package com.clip.api.user.controller;

import com.clip.api.docs.user.UserAccountV2Docs;
import com.clip.api.user.service.UserAccountService;
import com.clip.global.config.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserAccountV2Controller implements UserAccountV2Docs {

    private final UserAccountService userAccountV2Service;

    @Override
    public TokenProvider.Token getReissueToken(TokenProvider.RefreshToken refreshToken) {
        return userAccountV2Service.getReissueToken(refreshToken);
    }
}
