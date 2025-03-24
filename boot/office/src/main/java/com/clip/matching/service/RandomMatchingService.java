package com.clip.matching.service;

import com.clip.matching.client.RadomMatchingClient;
import com.clip.matching.client.dto.CreateRandomMatchingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RandomMatchingService {

    private final RadomMatchingClient radomMatchingClient;

    public CreateRandomMatchingDto callRandomMatchCreate(CreateRandomMatchingDto createRandomMatchingDto) {
        return radomMatchingClient.createRandomMatching(createRandomMatchingDto);
    }
}
