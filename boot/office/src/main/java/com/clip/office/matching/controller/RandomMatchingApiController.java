package com.clip.office.matching.controller;

import com.clip.office.matching.client.dto.CreateRandomMatchingApiDto;
import com.clip.office.matching.service.RandomMatchingApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/office/matching")
public class RandomMatchingApiController {

    private final RandomMatchingApiService randomMatchingApiService;

    @PostMapping("/create")
    public ResponseEntity<CreateRandomMatchingApiDto> create(@RequestBody CreateRandomMatchingApiDto createRandomMatchingApiDto) {
        return ResponseEntity.ok(randomMatchingApiService.callRandomMatchCreate(createRandomMatchingApiDto));
    }
}
