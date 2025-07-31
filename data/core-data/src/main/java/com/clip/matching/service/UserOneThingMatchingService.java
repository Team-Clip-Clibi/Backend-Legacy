package com.clip.matching.service;

import com.clip.matching.entity.OneThingMatching;
import com.clip.matching.entity.UserOneThingMatching;
import com.clip.matching.repository.UserOneThingMatchingRepository;
import com.clip.matching.repository.projection.ParticipantJobAndDietaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserOneThingMatchingService {
    private final UserOneThingMatchingRepository userOneThingMatchingRepository;

    public UserOneThingMatching save(UserOneThingMatching userOneThingMatching) {
        return userOneThingMatchingRepository.save(userOneThingMatching);
    }

    public boolean isScheduledOneThingMatchingExist(long userId) {
        return userOneThingMatchingRepository.findLastestAppliedOrConfirmStatusUserOneThingMatching(userId).isPresent();
    }

    public void deleteOneThingMatching(long userId) {
        userOneThingMatchingRepository.deleteOneThingMatching(userId);
    }

    public List<UserOneThingMatching> findTop5ConfirmedOrCompletedStatus(long userId) {
        return userOneThingMatchingRepository.findTop5ConfirmedOrCompletedStatus(userId, PageRequest.ofSize( 5));
    }

    public List<UserOneThingMatching> findDateTimeBetween(long userId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return userOneThingMatchingRepository.findDateTimeBetween(userId, startDateTime, endDateTime);
    }

    public List<UserOneThingMatching> findTop5ConfirmedOrCompletedStatus(long userId, LocalDateTime lastMatchingTime) {
        return userOneThingMatchingRepository.findTop5ConfirmedOrCompletedStatus(userId, lastMatchingTime, PageRequest.ofSize(5));
    }

    public List<ParticipantJobAndDietaryDto> findJobAndDietaryIn(List<OneThingMatching> onethingMatchings) {
        return userOneThingMatchingRepository.findJobAndDietaryIn(onethingMatchings);
    }
}
