package com.clip.office.matching.controller;

import com.clip.office.matching.controller.dto.CreateRandomMatchingDto;
import com.clip.office.matching.controller.dto.UpdateRandomMatchingDto;
import com.clip.office.matching.service.RandomMatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/office/matching")
public class RandomMatchingController {

    private final RandomMatchingService randomMatchingService;

    @PostMapping("/create")
    public ResponseEntity<CreateRandomMatchingDto> createRandomMatching(
            @RequestBody CreateRandomMatchingDto createRandomMatchingDto
    ) {
        return ResponseEntity.ok(randomMatchingService.createRandomMatching(createRandomMatchingDto));
    }

    @PutMapping("/{randomMatchingId}")
    public ResponseEntity<UpdateRandomMatchingDto> updateRandomMatching(
            @PathVariable Long randomMatchingId,
            @RequestBody UpdateRandomMatchingDto updateRandomMatchingDto
    ) {
        return ResponseEntity.ok(randomMatchingService.updateRandomMatching(randomMatchingId ,updateRandomMatchingDto));
    }

    @DeleteMapping("/{randomMatchingId}")
    public ResponseEntity<Void> deleteRandomMatching(
            @PathVariable Long randomMatchingId
    ) {
        randomMatchingService.deleteRandomMatching(randomMatchingId);
        return ResponseEntity.noContent().build();
    }
}
