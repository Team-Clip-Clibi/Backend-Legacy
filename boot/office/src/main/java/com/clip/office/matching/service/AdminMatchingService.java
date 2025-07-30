package com.clip.office.matching.service;

import com.clip.matching.entity.*;
import com.clip.matching.repository.OnethingMatchingRepository;
import com.clip.matching.repository.RandomMatchingRepository;
import com.clip.matching.repository.UserOneThingMatchingRepository;
import com.clip.matching.repository.UserRandomMatchingRepository;
import com.clip.matching.repository.projection.FirstParticipantKeywordDto;
import com.clip.matching.repository.projection.MatchingParticipantCntDto;
import com.clip.office.matching.controller.dto.*;
import com.clip.office.matching.controller.mapper.MatchingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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
    private final static int PAGE_SIZE = 30;
    private final OnethingMatchingRepository onethingMatchingRepository;
    private final UserOneThingMatchingRepository userOneThingMatchingRepository;
    private final RandomMatchingRepository randomMatchingRepository;
    private final UserRandomMatchingRepository userRandomMatchingRepository;
    private final MatchingMapper matchingMapper;

    @Transactional
    public OneThingMatching createOnethingMatching(CreateOnethingMatchingDto request) {
        OneThingMatching oneThingMatching = OneThingMatching.builder()
                .onethingDistrict(request.onethingDistrict())
                .restaurantName(request.restaurantName())
                .address(request.address())
                .dateTime(request.dateTime())
                .build();

        return onethingMatchingRepository.save(oneThingMatching);
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

        return randomMatchingRepository.save(randomMatching);
    }

    @Transactional(readOnly = true)
    public Slice<MatchingInfoDto> getOnethingMatchingList(LocalDate date, OnethingDistrict district, Integer page) {
        if (Objects.isNull(page)) page = 0;


        LocalDateTime startDateTime = date.atStartOfDay();
        LocalDateTime endDateTime = date.plusDays(1).atStartOfDay();

        Slice<OneThingMatching> matchingList = onethingMatchingRepository.findMatchingList(
                startDateTime,
                endDateTime,
                district,
                PageRequest.of(page, PAGE_SIZE));

        Map<Long, Long> idToParticipantCnt = userOneThingMatchingRepository.findParticipantCntIn(matchingList.getContent())
                .stream()
                .collect(Collectors.toMap(
                                MatchingParticipantCntDto::matchingId,
                                MatchingParticipantCntDto::participantCnt
                        )
                );

        Map<Long, OnethingKeyword> idToKeywords = userOneThingMatchingRepository.findFirstParticipantKeywords(matchingList.getContent())
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
        if (Objects.isNull(page)) page = 0;

        LocalDateTime startDateTime = date.atStartOfDay();
        LocalDateTime endDateTime = date.plusDays(1).atStartOfDay();

        Slice<RandomMatching> matchingList = randomMatchingRepository.findMatchingList(
                startDateTime,
                endDateTime,
                district,
                PageRequest.of(page, PAGE_SIZE));

        Map<Long, Long> idToParticipantCnt = userRandomMatchingRepository.findParticipantCntIn(matchingList.getContent())
                .stream()
                .collect(Collectors.toMap(
                        MatchingParticipantCntDto::matchingId,
                        MatchingParticipantCntDto::participantCnt)
                );

        return matchingMapper.toMatchingInfos(matchingList, idToParticipantCnt);
    }

    @Transactional(readOnly = true)
    public Slice<ParticipantInfoDto> getAssignedOnethingParticipantList(
            OnethingDistrict onethingDistrict,
            LocalDate localDate,
            Integer page
    ) {
        if (Objects.isNull(page)) page = 0;
        Slice<UserOneThingMatching> participantsFetchUser = userOneThingMatchingRepository.findAssignedParticipantsFetchUser(
                onethingDistrict,
                localDate,
                PageRequest.of(page, PAGE_SIZE)
        );
        return matchingMapper.toParticipantInfos(participantsFetchUser);
    }

    @Transactional(readOnly = true)
    public Slice<ParticipantInfoDto> getUnassignedOnethingParticipantList(
            OnethingDistrict onethingDistrict,
            LocalDate localDate,
            Integer page
    ) {
        if (Objects.isNull(page)) page = 0;
        Slice<UserOneThingMatching> participantsFetchUser = userOneThingMatchingRepository.findUnassignedParticipantsFetchUser(
                onethingDistrict,
                localDate,
                PageRequest.of(page, PAGE_SIZE)
        );
        return matchingMapper.toParticipantInfos(participantsFetchUser);
    }

    @Transactional
    public void deleteParticipantFromOnethingMatching(long userOnethingMatchingId) {
        UserOneThingMatching userOneThingMatching = userOneThingMatchingRepository.findById(userOnethingMatchingId)
                .orElseThrow(IllegalArgumentException::new);
        userOneThingMatching.deleteOnethingMatching();
        userOneThingMatchingRepository.save(userOneThingMatching);
    }

    @Transactional
    public void registerOnethingMatchingParticipants(RegisterOnethingParticipantDto request) {
        OneThingMatching oneThingMatching = onethingMatchingRepository.findById(request.onethingMatchingId())
                .orElseThrow(IllegalArgumentException::new);
        List<UserOneThingMatching> userOnethingMatchings = userOneThingMatchingRepository.findByIdsForUpdate(
                request.userOnethingMatchingIdList()
        );

        userOnethingMatchings.forEach(userOneThingMatching ->
                userOneThingMatching.updateOneThingMatching(oneThingMatching)
        );

        userOneThingMatchingRepository.saveAll(userOnethingMatchings);
    }
}
