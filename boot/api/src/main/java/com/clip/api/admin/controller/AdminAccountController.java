package com.clip.api.admin.controller;

import com.clip.api.admin.controller.dto.AdminLoginDto;
import com.clip.api.admin.service.AdminAccountService;
import com.clip.api.docs.admin.AdminAccountDocs;
import com.clip.global.config.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminAccountController implements AdminAccountDocs {

    private final AdminAccountService adminAccountService;

    @Override
    public TokenProvider.AdminToken loginAdminAccount(AdminLoginDto request){
        return adminAccountService.loginAdminAccount(request);
    }
}
