package com.clip.api.user.feign;

import com.clip.api.user.feign.dto.KakaoUserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakao-resource-server", url = "https://kapi.kakao.com")
public interface KakaoResourceFeignClient {

    @GetMapping("/v2/user/me")
    KakaoUserResponseDto getUserInfo(@RequestHeader("Authorization") String accessToken);
}
