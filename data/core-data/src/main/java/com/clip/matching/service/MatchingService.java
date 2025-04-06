package com.clip.matching.service;

import com.clip.matching.entity.OneThingMatching;
import com.clip.matching.entity.RandomMatching;
import com.clip.matching.exception.MatchingNotFoundException;
import com.clip.matching.repository.OneThingMatchingRepository;
import com.clip.matching.repository.RandomMatchingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchingService {
    private final OneThingMatchingRepository oneThingMatchingRepository;
    private final RandomMatchingRepository randomMatchingRepository;

    public OneThingMatching findOneThingMatching(final Long matchingId) {
        return oneThingMatchingRepository.findById(matchingId).orElseThrow(MatchingNotFoundException::new);
    }

    public RandomMatching findRandomMatching(final Long matchingId) {
        return randomMatchingRepository.findById(matchingId).orElseThrow(MatchingNotFoundException::new);
    }
}
