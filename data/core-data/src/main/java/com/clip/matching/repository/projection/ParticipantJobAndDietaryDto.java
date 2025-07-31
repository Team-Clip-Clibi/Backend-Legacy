package com.clip.matching.repository.projection;

import com.clip.user.entity.JobCategory;

public record ParticipantJobAndDietaryDto(
        long matchingId,
        JobCategory jobCategory,
        String dietaryOption
) {
}
