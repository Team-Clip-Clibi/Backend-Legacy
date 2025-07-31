package com.clip.matching.service;

import com.clip.global.exception.ResourceNotFoundException;
import com.clip.matching.entity.RandomMatching;
import com.clip.matching.entity.RandomMatchingStatus;
import com.clip.matching.entity.UserRandomMatching;
import com.clip.matching.repository.UserRandomMatchingRepository;
import com.clip.matching.repository.projection.ParticipantJobAndDietaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRandomMatchingService {
    private final UserRandomMatchingRepository userRandomMatchingRepository;

    public UserRandomMatching save(UserRandomMatching userRandomMatching) {
        return userRandomMatchingRepository.save(userRandomMatching);
    }

    public boolean isScheduledRandomMatchingExist(long userId) {
        return userRandomMatchingRepository.findLastestAppliedOrConfirmStatusUserRandomMatching(userId).isPresent();
    }

    public void deleteRandomMatching(long userId) {
        userRandomMatchingRepository.deleteRandomMatching(userId);
    }

    public boolean isDuplicatedMatching(long userId, LocalDateTime meetingTime) {
        return userRandomMatchingRepository.findUserRandomMatching(userId, meetingTime, RandomMatchingStatus.CONFIRMED).isPresent();
    }

    public UserRandomMatching findUserRandomMatching(long userId, Long randomOrderId) {
        return userRandomMatchingRepository.findUserRandomMatching(userId, randomOrderId)
                .orElseThrow(() -> new ResourceNotFoundException("UserRandomMatching", userId, randomOrderId));
    }

    public List<UserRandomMatching> findTop5ConfirmedOrCompletedStatus(long userId) {
        return userRandomMatchingRepository.findTop5ConfirmedOrCompletedStatus(userId, PageRequest.ofSize(5));
    }

    public List<UserRandomMatching> findDateTimeBetween(long userId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return userRandomMatchingRepository.findDateTimeBetween(userId, startDateTime, endDateTime);
    }

    public List<UserRandomMatching> findTop5ConfirmedOrCompletedStatus(long userId, LocalDateTime lastMatchingTime) {
        return userRandomMatchingRepository.findTop5ConfirmedOrCompletedStatus(userId, lastMatchingTime, PageRequest.ofSize(5));
    }

    public List<ParticipantJobAndDietaryDto> findJobAndDietaryIn(List<RandomMatching> randomMatchings) {
        return userRandomMatchingRepository.findJobAndDietaryIn(randomMatchings);
    }
}
