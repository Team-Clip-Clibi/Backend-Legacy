package com.clip.api.matching.service;

import com.clip.api.matching.controller.dto.MatchingReviewDto;
import com.clip.api.matching.controller.dto.MatchingType;
import com.clip.api.matching.controller.dto.ParticipantsInfoDto;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserMatchingReviewService {

    private final MatchingService matchingService;
    private final MatchingReviewService matchingReviewService;
    private final UserService userService;
    private final MatchingReviewMapper matchingReviewMapper;

    @Transactional
    public void saveMatchingReview(final Long userId, final Long matchingId,
                                   final MatchingType matchingType, final MatchingReviewDto request) {
        switch (matchingType) {
            case MatchingType.RANDOM -> handleRandomMatchingReview(userId, matchingId, request);
            case MatchingType.ONE_THING -> handleOneThingMatchingReview(userId, matchingId, request);
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

    public List<ParticipantsInfoDto> getMatchingParticipants(final Long userId, final Long matchingId, final MatchingType matchingType) {
        return switch (matchingType) {
            case MatchingType.RANDOM ->
                    matchingService.findRandomMatchingParticipants(matchingId).stream()
                            .map(userMatching -> ParticipantsInfoDto.builder()
                                    .id(userMatching.getUser().getId())
                                    .nickname(userMatching.getUser().getNickname()).build())
                            .toList();
            case MatchingType.ONE_THING ->
                matchingService.findOneThingMatchingParticipants(matchingId).stream()
                        .map(userOneThingMatching -> ParticipantsInfoDto.builder()
                                .id(userOneThingMatching.getUser().getId())
                                .nickname(userOneThingMatching.getUser().getNickname()).build())
                        .toList();
        };
    }
}

