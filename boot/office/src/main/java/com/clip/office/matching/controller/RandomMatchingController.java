package com.clip.office.matching.controller;

import com.clip.matching.entity.RandomMatching;
import com.clip.matching.service.RandomMatchingDataService;
import com.clip.office.matching.controller.dto.CreateRandomMatchingDto;
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

    private final RandomMatchingDataService randomMatchingDataService;

    @PostMapping("/create")
    public ResponseEntity<CreateRandomMatchingDto> createRandomMatching(@RequestBody RandomMatching randomMatching) {
        randomMatchingDataService.save(randomMatching);
        return ResponseEntity.ok(new CreateRandomMatchingDto());
    }
}
