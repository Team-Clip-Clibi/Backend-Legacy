package com.clip.api.matching.service;

import com.clip.api.matching.controller.dto.MatchingReviewDto;
import com.clip.api.matching.mapper.MatchingReviewMapper;
import com.clip.matching.entity.OneThingMatching;
import com.clip.matching.entity.RandomMatching;
import com.clip.matching.service.MatchingReviewService;
import com.clip.matching.service.MatchingService;
import com.clip.user.entity.User;
import com.clip.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserMatchingReviewService {

    private final MatchingService matchingService;
    private final MatchingReviewService matchingReviewService;
    private final UserService userService;
    private final MatchingReviewMapper matchingReviewMapper;

    @Transactional
    public void saveMatchingReview(final Long userId, final Long matchingId,
                                   final String matchingType, final MatchingReviewDto request) {
        switch (matchingType.toUpperCase()) {
            case "RANDOM" -> handleRandomMatchingReview(userId, matchingId, request);
            case "ONE_THING" -> handleOneThingMatchingReview(userId, matchingId, request);
            default -> throw new IllegalArgumentException("존재하지 않는 서비스 명입니다 : " + matchingType);
        }
    }

    private void handleRandomMatchingReview(final Long userId, final Long matchingId, final MatchingReviewDto request) {
        final User user = userService.findUser(userId);
        final RandomMatching randomMatching = matchingService.findRandomMatching(matchingId);
        matchingReviewService.save(matchingReviewMapper.toRandomMatchingReview(user, randomMatching, request));
    }

    private void handleOneThingMatchingReview(final Long userId, final Long matchingId, final MatchingReviewDto request) {
        final User user = userService.findUser(userId);
        final OneThingMatching oneThingMatching = matchingService.findOneThingMatching(matchingId);
        matchingReviewService.save(matchingReviewMapper.toOneThingMatchingReview(user, oneThingMatching, request));
    }
}

