package com.clip.matching.service;

import com.clip.matching.entity.OneThingMatchingReview;
import com.clip.matching.entity.RandomMatchingReview;
import com.clip.matching.repository.OneThingMatchingReviewRepository;
import com.clip.matching.repository.RandomMatchingReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchingReviewService {
    private final OneThingMatchingReviewRepository oneThingMatchingReviewRepository;
    private final RandomMatchingReviewRepository randomMatchingReviewRepository;

    public void save(final OneThingMatchingReview oneThingMatchingReview) {
        oneThingMatchingReviewRepository.save(oneThingMatchingReview);
    }

    public void save(final RandomMatchingReview randomMatchingReview) {
        randomMatchingReviewRepository.save(randomMatchingReview);
    }


}
