package com.clip.matching.service;

import com.clip.matching.repository.RandomMatchingReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RandomMatchingReviewService {
    private final RandomMatchingReviewRepository randomMatchingReviewRepository;

    public void deleteRandomMatchingReview(long userId) {
        randomMatchingReviewRepository.deleteRandomMatchingReview(userId);
    }
}
