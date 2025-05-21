package com.clip.matching.service;

import com.clip.matching.repository.OneThingMatchingReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OneThingMatchingReviewService {
    private final OneThingMatchingReviewRepository oneThingMatchingReviewRepository;

    public void deleteOneThingMatchingReview(long userId) {
        oneThingMatchingReviewRepository.deleteOneThingMatchingReview(userId);
    }
}
