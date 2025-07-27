package com.clip.api.user.feign;

import com.clip.api.user.feign.dto.KakaoLoginDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "kakaoLogin",
        url = "https://kapi.kakao.com"
)
public interface KakaoLoginFeign {
    @GetMapping("/v2/user/me")
    KakaoLoginDto getUserInfo(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken);
}
