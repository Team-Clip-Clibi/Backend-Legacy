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
import java.util.Optional;

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

    public Optional<UserOneThingMatching> findOptLatestUserOneThingMatching(long userId) {
        return userOneThingMatchingRepository.findLatestUserOneThingMatching(userId, LocalDateTime.now().minusHours(2));
    }

    public Optional<UserRandomMatching> findOptLatestUserRandomMatching(long userId) {
        return userRandomMatchingRepository.findLatestUserRandomMatching(userId, LocalDateTime.now().minusHours(2));
    }

    public List<UserRandomMatching> findAllUserRandomMatchings(long randomMatchingId) {
        return userRandomMatchingRepository.findUserRandomMatching(randomMatchingId);
    }

    public List<UserOneThingMatching> findAllUserOneThingMatchings(long oneThingMatchingId) {
        return userOneThingMatchingRepository.findUserOneThingMatching(oneThingMatchingId);
    }

    public void updateUserOneThingMatchingStatusChecked(long userId, long userOneThingMatchingId) {
        userOneThingMatchingRepository.updateStatusChecked(userId, userOneThingMatchingId);
    }

    public void updateUserRandomMatchingStatusChecked(long userId, long userRandomMatchingId) {
        userRandomMatchingRepository.updateStatusChecked(userId, userRandomMatchingId);
    }
}
