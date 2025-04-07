package com.clip.api.user.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "apple-resource-server", url = "https://appleid.apple.com/auth")
public interface AppleResourceFeignClient {

    @PostMapping(value = "token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    String getUserInfo(@RequestBody MultiValueMap<String, String> body);
}
