package com.clip.office.matching.client;

import com.clip.office.matching.client.dto.CreateRandomMatchingApiDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "randomMatchingApiClient", url = "${api.url}")
public interface RandomMatchingClient {

    @PostMapping("/match/random/create")
    CreateRandomMatchingApiDto createRandomMatching(@RequestBody CreateRandomMatchingApiDto createRandomMatchingApiDto);
}
