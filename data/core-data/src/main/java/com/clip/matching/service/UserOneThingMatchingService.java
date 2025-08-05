package com.clip.matching.service;

import com.clip.matching.entity.OneThingMatching;
import com.clip.matching.entity.OnethingDistrict;
import com.clip.matching.entity.UserOneThingMatching;
import com.clip.matching.repository.UserOneThingMatchingRepository;
import com.clip.matching.repository.projection.FirstParticipantKeywordDto;
import com.clip.matching.repository.projection.MatchingParticipantCntDto;
import com.clip.matching.repository.projection.ParticipantJobAndDietaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public List<MatchingParticipantCntDto> findParticipantCntIn(List<OneThingMatching> onethingMatchings) {
        return userOneThingMatchingRepository.findParticipantCntIn(onethingMatchings);
    }

    public List<FirstParticipantKeywordDto> findFirstParticipantKeywords(List<OneThingMatching> oneThingMatchings) {
        return userOneThingMatchingRepository.findFirstParticipantKeywords(oneThingMatchings);
    }

    public Slice<UserOneThingMatching> findAssignedParticipantsFetchUser(
            OnethingDistrict onethingDistrict,
            LocalDate localDate,
            int page
    ) {
        return userOneThingMatchingRepository.findAssignedParticipantsFetchUser(onethingDistrict, localDate, PageRequest.of(page, 30));
    }

    public Slice<UserOneThingMatching> findUnassignedParticipantsFetchUser(
            OnethingDistrict onethingDistrict,
            LocalDate localDate,
            int page
    ) {
        return userOneThingMatchingRepository.findUnassignedParticipantsFetchUser(onethingDistrict, localDate, PageRequest.of(page, 30));
    }

    public List<UserOneThingMatching> findByIdsForUpdate(List<Long> longs) {
        return userOneThingMatchingRepository.findByIdsForUpdate(longs);
    }

    public List<UserOneThingMatching> saveAll(List<UserOneThingMatching> userOneThingMatchings) {
        return userOneThingMatchingRepository.saveAll(userOneThingMatchings);
    }

    public UserOneThingMatching findById(long id) {
        return userOneThingMatchingRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }
}
