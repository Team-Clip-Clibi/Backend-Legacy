package com.clip.api.matching.controller;

import com.clip.api.docs.matching.MatchingReviewDocs;
import com.clip.api.matching.controller.dto.MatchingReviewDto;
import com.clip.api.matching.controller.dto.MatchingType;
import com.clip.api.matching.controller.dto.ParticipantsInfoDto;
import com.clip.api.matching.service.UserMatchingReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MatchingReviewController implements MatchingReviewDocs {

    private final UserMatchingReviewService userMatchingReviewService;

    @Override
    public void createMatchingReview(final Long matchingId, final MatchingType matchingType,
                                     final MatchingReviewDto request, final UserDetails userDetails) {
        userMatchingReviewService.saveMatchingReview(
                Long.parseLong(userDetails.getUsername()),
                matchingId,
                matchingType,
                request
        );
    }

    @Override
    public List<ParticipantsInfoDto> getMatchingParticipants(Long matchingId, MatchingType matchingType, UserDetails userDetails) {
        return userMatchingReviewService.getMatchingParticipants(
                Long.parseLong(userDetails.getUsername()),
                matchingId,
                matchingType
        );
    }

    @Override
    public List<MatchingReviewPopupDto> getMatchingReviewPopupInfo(UserDetails userDetails) {
        return userMatchingReviewService.getMatchingReviewPopupInfo(
                Long.parseLong(userDetails.getUsername())
        );
    }

}
