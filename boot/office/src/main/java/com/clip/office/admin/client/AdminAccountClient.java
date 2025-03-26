package com.clip.office.admin.client;

import com.clip.office.admin.client.dto.AdminLoginApiDto;
import com.clip.office.admin.client.dto.TokenProviderApiDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "adminAccount", url = "${api.url}")
public interface AdminAccountClient {

    @PostMapping("/office/login")
    TokenProviderApiDto loginAdminAccount(@RequestBody AdminLoginApiDto adminLoginApiDto);
}
