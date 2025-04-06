package com.clip.api.matching.controller;

import com.clip.api.docs.matching.MatchingReviewDocs;
import com.clip.api.matching.controller.dto.MatchingReviewDto;
import com.clip.api.matching.service.UserMatchingReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MatchingReviewController implements MatchingReviewDocs {

    private final UserMatchingReviewService userMatchingReviewService;

    @Override
    public void createMatchingReview(final Long matchingId, final String matchingType,
                                     final MatchingReviewDto request, final UserDetails userDetails) {
        userMatchingReviewService.saveMatchingReview(
                Long.parseLong(userDetails.getUsername()),
                matchingId,
                matchingType,
                request
        );
    }
}
