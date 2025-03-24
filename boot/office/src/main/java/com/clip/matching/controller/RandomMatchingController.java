package com.clip.matching.controller;

import com.clip.matching.client.dto.CreateRandomMatchingDto;
import com.clip.matching.service.RandomMatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/office/matching")
public class RandomMatchingController {

    private final RandomMatchingService randomMatchingService;

    @PostMapping("/create")
    public ResponseEntity<CreateRandomMatchingDto> create(@RequestBody CreateRandomMatchingDto createRandomMatchingDto) {
        return ResponseEntity.ok(randomMatchingService.callRandomMatchCreate(createRandomMatchingDto));
    }
}
