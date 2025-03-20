package com.clip.api.matching.controller;

import com.clip.api.docs.matching.MatchingDocs;
import com.clip.api.matching.controller.dto.CreateRandomMatchingDto;
import com.clip.api.matching.service.RandomMatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RandomMatchingController implements MatchingDocs {

    private final RandomMatchingService randomMatchingService;

    @Override
    public CreateRandomMatchingDto createRandomCommunity(CreateRandomMatchingDto createRandomMatchingDto) {
        return randomMatchingService.createRandomMatching(createRandomMatchingDto);
    }
}
