package com.clip.office.matching.controller.dto;

import com.clip.matching.entity.OnethingKeyword;
import com.clip.matching.entity.UserOneThingMatching;
import com.clip.user.entity.JobCategory;

import java.util.List;

public record ParticipantInfoDto(
        long userOnethingMatchingId,
        String nickname,
        String phoneNumber,
        OnethingKeyword onethingKeyword,
        JobCategory job,
        String language,
        List<UserOneThingMatching.PreferredDate> preferredDateList,
        String onethingTopic,
        String assignedRestaurantName
) {
}
