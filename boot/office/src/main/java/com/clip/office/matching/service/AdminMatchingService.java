package com.clip.office.matching.service;

import com.clip.matching.entity.*;
import com.clip.matching.repository.projection.FirstParticipantKeywordDto;
import com.clip.matching.repository.projection.MatchingParticipantCntDto;
import com.clip.matching.service.OnethingMatchingService;
import com.clip.matching.service.RandomMatchingService;
import com.clip.matching.service.UserOneThingMatchingService;
import com.clip.matching.service.UserRandomMatchingService;
import com.clip.office.matching.controller.dto.*;
import com.clip.office.matching.controller.mapper.MatchingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminMatchingService {
    private final static int MAX_CAPACITY = 6;
    private final OnethingMatchingService onethingMatchingService;
    private final UserOneThingMatchingService userOneThingMatchingService;
    private final RandomMatchingService randomMatchingService;
    private final UserRandomMatchingService userRandomMatchingService;
    private final MatchingMapper matchingMapper;

    @Transactional
    public OneThingMatching createOnethingMatching(CreateOnethingMatchingDto request) {
        OneThingMatching oneThingMatching = OneThingMatching.builder()
                .onethingDistrict(request.onethingDistrict())
                .restaurantName(request.restaurantName())
                .address(request.address())
                .dateTime(request.dateTime())
                .build();

        return onethingMatchingService.save(oneThingMatching);
    }

    @Transactional
    public RandomMatching createRandomMatching(CreateRandomMatchingDto request) {
        RandomMatching randomMatching = RandomMatching.builder()
                .dateTime(request.dateTime())
                .address(request.address())
                .randomDistrict(request.randomDistrict())
                .totalCapacity(MAX_CAPACITY)
                .restaurantName(request.restaurantName())
                .build();

        return randomMatchingService.save(randomMatching);
    }

    @Transactional(readOnly = true)
    public Slice<MatchingInfoDto> getOnethingMatchingList(LocalDate date, OnethingDistrict district, Integer page) {
        LocalDateTime startDateTime = Objects.isNull(date) ? null : date.atStartOfDay();
        LocalDateTime endDateTime = Objects.isNull(date) ? null : date.plusDays(1).atStartOfDay();

        Slice<OneThingMatching> matchingList = onethingMatchingService.findMatchingList(
                startDateTime,
                endDateTime,
                district,
                page);


        Map<Long, Long> idToParticipantCnt = userOneThingMatchingService.findParticipantCntIn(matchingList.getContent())
                .stream()
                .collect(Collectors.toMap(
                                MatchingParticipantCntDto::matchingId,
                                MatchingParticipantCntDto::participantCnt
                        )
                );

        Map<Long, OnethingKeyword> idToKeywords = userOneThingMatchingService.findFirstParticipantKeywords(matchingList.getContent())
                .stream()
                .collect(Collectors.toMap(
                                FirstParticipantKeywordDto::matchingId,
                                FirstParticipantKeywordDto::keyWord
                        )
                );

        return matchingMapper.toMatchingInfos(matchingList, idToParticipantCnt, idToKeywords);
    }

    @Transactional(readOnly = true)
    public Slice<MatchingInfoDto> getRandomMatchingList(LocalDate date, RandomDistrict district, Integer page) {
        LocalDateTime startDateTime = Objects.isNull(date) ? null : date.atStartOfDay();
        LocalDateTime endDateTime = Objects.isNull(date) ? null : date.plusDays(1).atStartOfDay();


        Slice<RandomMatching> matchingList = randomMatchingService.findMatchingList(startDateTime, endDateTime, district, page);

        Map<Long, Long> idToParticipantCnt = userRandomMatchingService.findParticipantCntIn(matchingList.getContent())
                .stream()
                .collect(Collectors.toMap(
                        MatchingParticipantCntDto::matchingId,
                        MatchingParticipantCntDto::participantCnt)
                );

        return matchingMapper.toMatchingInfos(matchingList, idToParticipantCnt);
    }

    @Transactional(readOnly = true)
    public Slice<ParticipantInfoDto> getAssignedOnethingParticipantList(
            Long onethingMatchingId,
            Integer page
    ) {
        Slice<UserOneThingMatching> participantsFetchUser = userOneThingMatchingService.findAssignedParticipantsFetchUser(
                onethingMatchingId,
                page
        );
        return matchingMapper.toParticipantInfos(participantsFetchUser);
    }

    @Transactional(readOnly = true)
    public Slice<ParticipantInfoDto> getUnassignedOnethingParticipantList(
            OnethingDistrict onethingDistrict,
            LocalDate localDate,
            Integer page
    ) {
        Slice<UserOneThingMatching> participantsFetchUser = userOneThingMatchingService.findUnassignedParticipantsFetchUser(
                onethingDistrict,
                localDate,
                page
        );
        return matchingMapper.toParticipantInfos(participantsFetchUser);
    }

    @Transactional
    public void deleteParticipantFromOnethingMatching(long userOnethingMatchingId) {
        UserOneThingMatching userOneThingMatching = userOneThingMatchingService.findById(userOnethingMatchingId);
        userOneThingMatching.deleteOnethingMatching();
        userOneThingMatchingService.save(userOneThingMatching);
    }

    @Transactional
    public void registerOnethingMatchingParticipants(RegisterOnethingParticipantDto request) {
        OneThingMatching oneThingMatching = onethingMatchingService.findById(request.onethingMatchingId());
        List<UserOneThingMatching> userOnethingMatchings = userOneThingMatchingService.findByIdsForUpdate(
                request.userOnethingMatchingIdList()
        );

        userOnethingMatchings.forEach(userOneThingMatching ->
                userOneThingMatching.updateOneThingMatching(oneThingMatching)
        );

        userOneThingMatchingService.saveAll(userOnethingMatchings);
    }

    public void deleteOnethingMatching(Long onethingMatchingId) {
        onethingMatchingService.delete(onethingMatchingService.findById(onethingMatchingId));
    }

    public void deleteRandomMatching(Long randomMatchingId) {
        randomMatchingService.delete(randomMatchingService.findById(randomMatchingId));
    }
}
