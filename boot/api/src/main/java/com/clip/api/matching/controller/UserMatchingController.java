package com.clip.api.matching.controller;

import com.clip.api.docs.matching.UserMatchingDocs;
import com.clip.api.matching.controller.dto.MatchingSummaryDto;
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
}
