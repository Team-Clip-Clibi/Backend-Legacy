package com.clip.office.question.controller.dto;

import com.clip.office.matching.controller.dto.MatchingType;

import java.time.LocalDateTime;

public record MatchingQuestionInfoDto(
        long matchingId,
        String restaurantName,
        String address,
        LocalDateTime dateTime,
        MatchingType matchingType
) {
}
