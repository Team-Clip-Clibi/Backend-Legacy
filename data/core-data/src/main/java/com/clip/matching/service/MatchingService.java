package com.clip.matching.service;

import com.clip.global.exception.NoContentAvailableException;
import com.clip.global.exception.ResourceNotFoundException;
import com.clip.matching.entity.*;
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
    private final RandomMatchingCapacityRepository randomMatchingCapacityRepository;

    public OneThingMatching findOneThingMatching(final Long matchingId) {
        return oneThingMatchingRepository.findById(matchingId).orElseThrow(()->new ResourceNotFoundException("oneThingMatching", matchingId));
    }

    public RandomMatching findRandomMatching(final Long matchingId) {
        return randomMatchingRepository.findById(matchingId).orElseThrow(()->new ResourceNotFoundException("randomMatching", matchingId));
    }

    public List<RandomMatchingCapacity> findRandomMatchingCapacitiesWithDistrict(RandomDistrict district, LocalDateTime matchingTime, LocalDateTime matchingTimeEnd) {
        return randomMatchingCapacityRepository.findRandomMatchingCapacitiesWithDistrict(district, matchingTime, matchingTimeEnd);
    }

    public RandomMatchingCapacity findRandomMatchingCapacity(final Long randomMatchingId) {
        return randomMatchingCapacityRepository.findRandomMatchingCapacity(randomMatchingId)
                .orElseThrow(()->new ResourceNotFoundException("randomMatchingCapacity", randomMatchingId));
    }

    public List<UserOneThingMatching> findUserOneThingMatchings(final Long userId) {
        return userOneThingMatchingRepository.findUserOneThingMatching(userId, LocalDateTime.now(), OneThingMatchingStatus.CONFIRMED);
    }

    public List<UserRandomMatching> findUserRandomMatchings(final Long userId) {
        return userRandomMatchingRepository.findUserRandomMatching(userId, LocalDateTime.now(), RandomMatchingStatus.CONFIRMED);
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

    public List<UserOneThingMatching> findAllConfirmedOneThingMatching(long userId, OneThingMatchingStatus matchingStatus) {
        return userOneThingMatchingRepository.findConfirmedUserOneThingMatching(userId, matchingStatus);
    }

    public List<UserRandomMatching> findAllConfirmedRandomMatching(long userId, RandomMatchingStatus matchingStatus) {
        return userRandomMatchingRepository.findConfirmedUserRandomMatching(userId, matchingStatus);
    }

    public List<UserOneThingMatching> findAllAppliedOneThingMatching(long userId, OneThingMatchingStatus matchingStatus) {
        return userOneThingMatchingRepository.findAppliedUserOneThingMatching(userId, matchingStatus, OneThingOrderStatus.DONE);
    }

    public List<UserRandomMatching> findAllAppliedRandomMatching(long userId, RandomMatchingStatus matchingStatus) {
        return userRandomMatchingRepository.findAppliedUserRandomMatching(userId, matchingStatus, RandomOrderStatus.DONE);
    }

    public List<MatchingProjectionDto> findAllMatchings(RandomMatchingStatus matchingStatus, LocalDateTime lastMeetingTime, long userId) {
        List<MatchingProjectionDto> matchings = userMatchingRepository.findAllMatchingsByStatus(matchingStatus, lastMeetingTime, userId, PageRequest.ofSize(PAGE_SIZE));
        if (matchings.isEmpty()) {
            throw new NoContentAvailableException("userMatchings", userId);
        }
        return matchings;
    }

    public UserRandomMatching findUserRandomMatching(long id) {
        return userRandomMatchingRepository.findUserRandomMatchingWithFetch(id)
                .orElseThrow(() -> new ResourceNotFoundException("userRandomMatching", id));
    }

    public UserOneThingMatching findUserOneThingMatchingWithMatchingInfo(long id) {
        return userOneThingMatchingRepository.findUserOneThingMatchingWithMatchingInfo(id)
                .orElseThrow(() -> new ResourceNotFoundException("userOneThingMatching MatchingInfo", id));
    }

    public UserOneThingMatching findUserOneThingMatchingWithPaymentInfo(long id) {
        return userOneThingMatchingRepository.findUserOneThingMatchingWithPaymentInfo(id)
                .orElseThrow(() -> new ResourceNotFoundException("userOneThingMatching PaymentInfo", id));
    }

    public void cancelUserOneThingMatching(long id) {
        userOneThingMatchingRepository.updateMatchingStatus(id, OneThingMatchingStatus.CANCELED);
    }

    public void cancelUserRandomMatching(long id) {
        userRandomMatchingRepository.updateMatchingStatus(id, RandomMatchingStatus.CANCELED);
    }
}
