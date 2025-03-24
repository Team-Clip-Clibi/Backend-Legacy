package com.clip.matching.client;

import com.clip.matching.client.dto.CreateRandomMatchingDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "matchingApiClient", url = "${api.url}")
public interface RadomMatchingClient {

    @PostMapping("/match/random/create")
    CreateRandomMatchingDto createRandomMatching(@RequestBody CreateRandomMatchingDto createRandomMatchingDto);
}
