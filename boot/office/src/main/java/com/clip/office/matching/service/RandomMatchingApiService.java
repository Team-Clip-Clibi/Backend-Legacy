package com.clip.office.matching.service;

import com.clip.office.matching.client.RandomMatchingClient;
import com.clip.office.matching.client.dto.CreateRandomMatchingApiDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RandomMatchingApiService {

    private final RandomMatchingClient randomMatchingClient;

    public CreateRandomMatchingApiDto callRandomMatchCreate(CreateRandomMatchingApiDto createRandomMatchingApiDto) {
        return randomMatchingClient.createRandomMatching(createRandomMatchingApiDto);
    }
}
