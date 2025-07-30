package com.clip.office.matching.controller.dto;

import java.util.List;

public record RegisterOnethingParticipantDto(
        long onethingMatchingId,
        List<Long> userOnethingMatchingIdList
) {
}
