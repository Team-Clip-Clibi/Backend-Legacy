package com.clip.api.matching.service;

import com.clip.api.matching.controller.dto.*;
import com.clip.api.matching.mapper.MatchingMapper;
import com.clip.api.matching.service.exception.NotExistAnyMatchingException;
import com.clip.matching.entity.*;
import com.clip.matching.service.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserMatchingService {

    private final MatchingService matchingService;
    private final MatchingMapper matchingMapper;

    public MatchingSummaryDto getUserMatchings(final long userId) {
        List<UserOneThingMatching> userOneThingMatchings = matchingService.findUserOneThingMatchings(userId);
        List<UserRandomMatching> userRandomMatchings = matchingService.findUserRandomMatchings(userId);
        return MatchingSummaryDto.builder()
                .oneThingMatchings(userOneThingMatchings.stream()
                        .map(matchingMapper::toDto)
                        .toList())
                .randomMatchings(userRandomMatchings.stream()
                        .map(matchingMapper::toDto)
                        .toList())
                .build();
    }

    public MatchingProgressStatusDto getUserMatchingStatus(long userId) {
        Optional<UserOneThingMatching> myOptUserOneThingMatching = matchingService.findOptLatestUserOneThingMatching(userId, LocalDateTime.now().minusHours(2));
        Optional<UserRandomMatching> myOptUserRandomMatching = matchingService.findOptLatestUserRandomMatching(userId, LocalDateTime.now().minusHours(2));

        List<UserOneThingMatching> myOneThingMatchingGroup = myOptUserOneThingMatching.map(
                matching -> matchingService.findAllUserOneThingMatchings(matching.getOneThingMatching().getId())
        ).orElse(List.of());

        List<UserRandomMatching> myRandomMatchingGroup = myOptUserRandomMatching.map(
                matching -> matchingService.findAllUserRandomMatchings(matching.getRandomMatching().getId())
        ).orElse(List.of());

        if (myOptUserOneThingMatching.isEmpty() && myOptUserRandomMatching.isEmpty()) {
            throw new NotExistAnyMatchingException();
        } else if (myOptUserOneThingMatching.isPresent() && myOptUserRandomMatching.isPresent()) {
            if (isOneThingMeetingBeforeRandomMeeting(myOptUserOneThingMatching, myOptUserRandomMatching)) {
                return getOneThingMatchingProgressStatusDto(myOptUserOneThingMatching, myOneThingMatchingGroup);
            }else {
                return getRandomMatchingProgressStatusDto(myOptUserRandomMatching, myRandomMatchingGroup);
            }
        }else{
            if (myOptUserOneThingMatching.isPresent()) {
                return getOneThingMatchingProgressStatusDto(myOptUserOneThingMatching, myOneThingMatchingGroup);
            }else {
                return getRandomMatchingProgressStatusDto(myOptUserRandomMatching, myRandomMatchingGroup);
            }
        }
    }

    private static boolean isOneThingMeetingBeforeRandomMeeting(
            Optional<UserOneThingMatching> optUserOneThingMatching,
            Optional<UserRandomMatching> optUserRandomMatching
    ) {
        return optUserOneThingMatching.get()
                .getOneThingMatching()
                .getMeetingTime()
                .isBefore(
                        optUserRandomMatching.get()
                                .getRandomMatching()
                                .getMeetingTime()
                );
    }

    private static MatchingProgressStatusDto getRandomMatchingProgressStatusDto(
            Optional<UserRandomMatching> myOptUserRandomMatching,
            List<UserRandomMatching> myRandomMatchingGroup
    ) {
        UserRandomMatching userRandomMatching = myOptUserRandomMatching.get();
        boolean isProgress = userRandomMatching.getRandomMatching().getMeetingTime().isBefore(LocalDateTime.now());

        List<String> shuffledNicknames = getShuffledResultIfExecuted(
                myRandomMatchingGroup,
                isProgress,
                oneThing -> oneThing.getUser().getNickname()
        );

        return MatchingProgressStatusDto.builder()
                .isCheckedMatchingStart(userRandomMatching.isCheckedMatchingStart())
                .matchingType(MatchingType.RANDOM)
                .matchingId(userRandomMatching.getId())
                .nicknameList(shuffledNicknames)
                .latestMatchingDateTime(userRandomMatching.getRandomMatching().getMeetingTime())
                .build();
    }

    private static MatchingProgressStatusDto getOneThingMatchingProgressStatusDto(
            Optional<UserOneThingMatching> myOptUserOneThingMatching,
            List<UserOneThingMatching> myOneThingMatchingGroup
    ) {
        UserOneThingMatching userOneThingMatching = myOptUserOneThingMatching.get();
        boolean isProgress = userOneThingMatching.getOneThingMatching().getMeetingTime().isBefore(LocalDateTime.now());

        List<String> shuffledNicknames = getShuffledResultIfExecuted(
                myOneThingMatchingGroup,
                isProgress,
                oneThing -> oneThing.getUser().getNickname()
        );

        List<String> shuffledQuiz = getShuffledResultIfExecuted(
                myOneThingMatchingGroup,
                isProgress,
                UserOneThingMatching::getMyQuizContent
        );

        Map<String, String> oneThingMap = getNicknameAndOneThingContentMap(myOneThingMatchingGroup, isProgress);

        return MatchingProgressStatusDto.builder()
                .isCheckedMatchingStart(userOneThingMatching.isCheckedMatchingStart())
                .matchingType(MatchingType.ONE_THING)
                .matchingId(userOneThingMatching.getId())
                .nicknameList(shuffledNicknames)
                .quizList(shuffledQuiz)
                .latestMatchingDateTime(userOneThingMatching.getOneThingMatching().getMeetingTime())
                .oneThingMap(oneThingMap)
                .build();
    }

    private static Map<String, String> getNicknameAndOneThingContentMap(List<UserOneThingMatching> oneThingMatchings, boolean isExecute) {
        return oneThingMatchings.stream().filter(oneThingMatching -> isExecute)
                .collect(Collectors.toMap(
                        userOneThing -> userOneThing.getUser().getNickname(),
                        UserOneThingMatching::getMyOneThingContent)
                );
    }

    private static <T, R> List<R> getShuffledResultIfExecuted(
            List<T> targetList,
            boolean isExecute,
            Function<T, R> func
    ) {
        return targetList.stream()
                .filter(a -> isExecute)
                .map(func)
                .collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
                    Collections.shuffle(list);
                    return list;
                }));
    }

    public void updateMatchingStatusChecked(long userId, MatchingType matchingType, long matchingId) {
        switch (matchingType) {
            case ONE_THING -> matchingService.updateUserOneThingMatchingStatusChecked(userId, matchingId);
            case RANDOM -> matchingService.updateUserRandomMatchingStatusChecked(userId, matchingId);
        }
    }

    public MatchingOverviewDto getMatchingOverview(Long userId) {
        // 매칭 신청한 매칭 수와 확정된 매칭 수 계산
        long appliedMatchingCount =
                matchingService.findAllAppliedOneThingMatching(userId, MatchingStatus.APPLIED).size() +
                        matchingService.findAllAppliedRandomMatching(userId, MatchingStatus.APPLIED).size();

        List<UserOneThingMatching> confirmedOneThingMatchings =
                matchingService.findAllConfirmedOneThingMatching(userId, MatchingStatus.CONFIRMED);
        List<UserRandomMatching> confirmedRandomMatchings =
                matchingService.findAllConfirmedRandomMatching(userId, MatchingStatus.CONFIRMED);
        long confirmedMatchingCount = confirmedOneThingMatchings.size() + confirmedRandomMatchings.size();

        // 매칭 안내문 확인 여부
        boolean isAllNoticeRead = (confirmedOneThingMatchings.isEmpty() || confirmedOneThingMatchings.stream().allMatch(UserOneThingMatching::isNoticeRead)) &&
                        (confirmedRandomMatchings.isEmpty() || confirmedRandomMatchings.stream().allMatch(UserRandomMatching::isNoticeRead));

        // 다음 매칭 날짜 계산
        Optional<LocalDate> oneThingDate = matchingService.findOptLatestUserOneThingMatching(userId, LocalDateTime.now())
                .map(match -> match.getOneThingMatching().getMeetingTime().toLocalDate());

        Optional<LocalDate> randomDate = matchingService.findOptLatestUserRandomMatching(userId, LocalDateTime.now())
                .map(match -> match.getRandomMatching().getMeetingTime().toLocalDate());

        // 두 날짜 중 더 이른 날짜 선택
        LocalDate nextMatchingDate = Stream.of(oneThingDate, randomDate)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .min(LocalDate::compareTo)
                .orElse(null);

        return MatchingOverviewDto.builder()
                .appliedMatchingCount(appliedMatchingCount)
                .confirmedMatchingCount(confirmedMatchingCount)
                .nextMatchingDate(nextMatchingDate)
                .isAllNoticeRead(isAllNoticeRead)
                .build();
    }

    public List<MatchingDto> getMatchings(MatchingStatus matchingStatus, LocalDateTime lastMeetingTime, long userId) {
        return matchingMapper.toDto(matchingService.findAllMatchings(matchingStatus, lastMeetingTime, userId));
    }

}
