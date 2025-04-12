package com.clip.matching.service;

import com.clip.matching.entity.OneThingMatching;
import com.clip.matching.entity.RandomMatching;
import com.clip.matching.entity.UserOneThingMatching;
import com.clip.matching.entity.UserRandomMatching;
import com.clip.matching.exception.MatchingNotFoundException;
import com.clip.matching.repository.OneThingMatchingRepository;
import com.clip.matching.repository.RandomMatchingRepository;
import com.clip.matching.repository.UserOneThingMatchingRepository;
import com.clip.matching.repository.UserRandomMatchingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchingService {
    private final OneThingMatchingRepository oneThingMatchingRepository;
    private final RandomMatchingRepository randomMatchingRepository;
    private final UserOneThingMatchingRepository userOneThingMatchingRepository;
    private final UserRandomMatchingRepository userRandomMatchingRepository;

    public OneThingMatching findOneThingMatching(final Long matchingId) {
        return oneThingMatchingRepository.findById(matchingId).orElseThrow(MatchingNotFoundException::new);
    }

    public RandomMatching findRandomMatching(final Long matchingId) {
        return randomMatchingRepository.findById(matchingId).orElseThrow(MatchingNotFoundException::new);
    }

    public List<UserOneThingMatching> findUserOneThingMatchings(final Long userId) {
        return userOneThingMatchingRepository.findUserOneThingMatching(userId, LocalDateTime.now());
    }

    public List<UserRandomMatching> findUserRandomMatchings(final Long userId) {
        return userRandomMatchingRepository.findUserRandomMatching(userId, LocalDateTime.now());
    }
}
