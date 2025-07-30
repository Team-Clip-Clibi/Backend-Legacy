package com.clip.office.matching.controller.dto;

import com.clip.matching.entity.OnethingKeyword;

import java.time.LocalDateTime;

public record MatchingInfoDto(
        long matchingId,
        String restaurantName,
        String address,
        LocalDateTime dateTime,
        MatchingType matchingType,
        int participantCnt,
        OnethingKeyword onethingKeyword
) {
}
