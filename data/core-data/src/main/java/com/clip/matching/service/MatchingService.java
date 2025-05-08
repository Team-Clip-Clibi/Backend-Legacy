package com.clip.matching.service;

import com.clip.matching.entity.*;
import com.clip.matching.exception.MatchingNotFoundException;
import com.clip.matching.exception.NotExistMatchingException;
import com.clip.matching.repository.*;
import com.clip.matching.repository.projection.MatchingProjectionDto;
import com.clip.order.entity.OneThingOrderStatus;
import com.clip.order.entity.RandomOrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MatchingService {
    private final int PAGE_SIZE = 50;
    private final OneThingMatchingRepository oneThingMatchingRepository;
    private final RandomMatchingRepository randomMatchingRepository;
    private final UserOneThingMatchingRepository userOneThingMatchingRepository;
    private final UserRandomMatchingRepository userRandomMatchingRepository;
    private final UserMatchingRepository userMatchingRepository;

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

    public Optional<UserOneThingMatching> findOptLatestUserOneThingMatching(long userId, LocalDateTime dateTime) {
        return userOneThingMatchingRepository.findLatestUserOneThingMatching(userId, dateTime);
    }

    public Optional<UserRandomMatching> findOptLatestUserRandomMatching(long userId, LocalDateTime dateTime) {
        return userRandomMatchingRepository.findLatestUserRandomMatching(userId, dateTime);
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

    public List<UserOneThingMatching> findAllConfirmedOneThingMatching(long userId, MatchingStatus matchingStatus) {
        return userOneThingMatchingRepository.findConfirmedUserOneThingMatching(userId, matchingStatus);
    }

    public List<UserRandomMatching> findAllConfirmedRandomMatching(long userId, MatchingStatus matchingStatus) {
        return userRandomMatchingRepository.findConfirmedUserRandomMatching(userId, matchingStatus);
    }

    public List<UserOneThingMatching> findAllAppliedOneThingMatching(long userId, MatchingStatus matchingStatus) {
        return userOneThingMatchingRepository.findAppliedUserOneThingMatching(userId, matchingStatus, OneThingOrderStatus.DONE);
    }

    public List<UserRandomMatching> findAllAppliedRandomMatching(long userId, MatchingStatus matchingStatus) {
        return userRandomMatchingRepository.findAppliedUserRandomMatching(userId, matchingStatus, RandomOrderStatus.DONE);
    }

    public List<MatchingProjectionDto> findAllMatchings(MatchingStatus matchingStatus, LocalDateTime lastMeetingTime, long userId) {
        LocalDateTime referenceTime = lastMeetingTime != null ? lastMeetingTime : LocalDateTime.now();
        LocalDateTime sixMonthsAgo = referenceTime.minusMonths(6);
        List<MatchingProjectionDto> matchings = userMatchingRepository.findAllMatchingsByStatus(matchingStatus, referenceTime, sixMonthsAgo, userId,PageRequest.ofSize(PAGE_SIZE));
        if (matchings.isEmpty()) {
            throw new NotExistMatchingException();
        }
        return matchings;
    }
}
