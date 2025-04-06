package com.clip.office.matching.controller;

import com.clip.office.matching.controller.dto.CreateRandomMatchingDto;
import com.clip.office.matching.service.RandomMatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/office/matching")
public class RandomMatchingController {

    private final RandomMatchingService randomMatchingService;

    @PostMapping("/create")
    public ResponseEntity<CreateRandomMatchingDto> createRandomMatching(@RequestBody CreateRandomMatchingDto createRandomMatchingDto) {
        return ResponseEntity.ok(randomMatchingService.createRandomMatching(createRandomMatchingDto));
    }
}
