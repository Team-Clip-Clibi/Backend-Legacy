package com.clip.api.matching.controller;

import com.clip.api.docs.matching.UserMatchingDocs;
import com.clip.api.matching.controller.dto.MatchingProgressStatusDto;
import com.clip.api.matching.controller.dto.MatchingSummaryDto;
import com.clip.api.matching.controller.dto.MatchingType;
import com.clip.api.matching.service.UserMatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserMatchingController implements UserMatchingDocs {

    private final UserMatchingService userMatchingService;

    @Override
    public MatchingSummaryDto getMatchingSummaryInfo(UserDetails userDetails) {
        return userMatchingService.getUserMatchings(Long.parseLong(userDetails.getUsername()));
    }

    @Override
    public MatchingProgressStatusDto getMatchingStatus(UserDetails userDetails) {
        return userMatchingService.getUserMatchingStatus(Long.parseLong(userDetails.getUsername()));
    }

    @Override
    public void updateMatchingStatusChecked(MatchingType matchingType, long matchingId, UserDetails userDetails) {
        userMatchingService.updateMatchingStatusChecked(Long.parseLong(userDetails.getUsername()), matchingType, matchingId);
    }
}
