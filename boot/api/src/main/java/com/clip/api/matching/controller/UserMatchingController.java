package com.clip.api.matching.controller;

import com.clip.api.docs.matching.UserMatchingDocs;
import com.clip.api.matching.controller.dto.*;
import com.clip.api.matching.service.UserMatchingService;
import com.clip.matching.entity.OneThingMatchingStatus;
import com.clip.matching.entity.RandomMatchingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserMatchingController implements UserMatchingDocs {

    private final UserMatchingService userMatchingService;

    @Override
    public MatchingSummaryDto getMatchingSummaryInfo(UserDetails userDetails) {
        return userMatchingService.getUserMatchings(Long.parseLong(userDetails.getUsername()));
    }

    @Override
    public MatchingProgressInfoDto getMatchingProgressInfo(MatchingType matchingType, long id, UserDetails userDetails) {
        return userMatchingService.getUserMatchingProgressInfo(matchingType, id, Long.parseLong(userDetails.getUsername()));
    }

    @Override
    public void updateMatchingStatusChecked(MatchingType matchingType, long matchingId, UserDetails userDetails) {
        userMatchingService.updateMatchingStatusChecked(Long.parseLong(userDetails.getUsername()), matchingType, matchingId);
    }

    @Override
    public MatchingOverviewDto getMatchingOverview(UserDetails userDetails) {
        return userMatchingService.getMatchingOverview(Long.parseLong(userDetails.getUsername()));
    }

    @Override
    public List<MatchingDto> findMatchings(RandomMatchingStatus matchingStatus, LocalDateTime lastMeetingTime, UserDetails userDetails) {
        return userMatchingService.getMatchings(matchingStatus, lastMeetingTime, Long.parseLong(userDetails.getUsername()));
    }

    @Override
    public MatchingDetailDto getMatchingDetail(UserDetails userDetails, MatchingType matchingType, long id) {
        return userMatchingService.getMatchingDetail(
                Long.parseLong(userDetails.getUsername()), matchingType, id);
    }

    @Override
    public void cancelMatching(UserDetails userDetails, MatchingType matchingType, long id) {
        userMatchingService.cancelMatching(Long.parseLong(userDetails.getUsername()), matchingType, id);
    }

    @Override
    public void updateLastMinutesAndSendNotification(UserDetails userDetails, MatchingType matchingType, long id, LateMinutesUpdateDto lateMinutesUpdateDto) {
        userMatchingService.updateLastMinutesAndSendNotification(
                Long.parseLong(userDetails.getUsername()), matchingType, id, lateMinutesUpdateDto.getLateMinutes());
    }

    @Override
    public List<MatchingNoticeDto> getMatchingNotice(LocalDateTime lastMeetingTime, UserDetails userDetails) {
        return userMatchingService.getMatchingNotice(lastMeetingTime, Long.parseLong(userDetails.getUsername()));
//        return List.of(
//                MatchingNoticeDto.builder()
//                        .matchingId(1L)
//                        .meetingTime(LocalDateTime.of(2025, 7, 9, 12, 30))
//                        .matchingStatus(OneThingMatchingStatus.CONFIRMED)
//                        .matchingType(MatchingType.ONE_THING)
//                        .myOneThingContent("다들 면접 준비는 어떻게 하시나요?")
//                        .restaurantName("강남 맛집 김치찌개")
//                        .location("강남구 역삼동 123-45")
//                        .menuCategory("한식")
//                        .jobInfos(List.of(
//                                MatchingNoticeDto.JobInfo.builder()
//                                        .jobName("개발자")
//                                        .count(2)
//                                        .build(),
//                                MatchingNoticeDto.JobInfo.builder()
//                                        .jobName("디자이너")
//                                        .count(1)
//                                        .build(),
//                                MatchingNoticeDto.JobInfo.builder()
//                                        .jobName("기획자")
//                                        .count(1)
//                                        .build()
//                        ))
//                        .dietaryList(List.of("비건","글루텐프리"))
//                        .build(),
//
//                MatchingNoticeDto.builder()
//                        .matchingId(2L)
//                        .meetingTime(LocalDateTime.of(2025, 6, 27, 19, 0))
//                        .matchingStatus(RandomMatchingStatus.COMPLETED)
//                        .matchingType(MatchingType.RANDOM)
//                        .myOneThingContent("다들 면접 준비는 어떻게 하시나요?")
//                        .restaurantName("홍대 파스타 하우스")
//                        .location("마포구 홍익로 67-8")
//                        .menuCategory("양식")
//                        .jobInfos(List.of(
//                                MatchingNoticeDto.JobInfo.builder()
//                                        .jobName("마케터")
//                                        .count(2)
//                                        .build(),
//                                MatchingNoticeDto.JobInfo.builder()
//                                        .jobName("개발자")
//                                        .count(1)
//                                        .build(),
//                                MatchingNoticeDto.JobInfo.builder()
//                                        .jobName("영업")
//                                        .count(2)
//                                        .build()
//                        ))
//                        .dietaryList(List.of("비건","글루텐프리"))
//                        .build(),
//
//                MatchingNoticeDto.builder()
//                        .matchingId(3L)
//                        .meetingTime(LocalDateTime.of(2025, 6, 26, 13, 15))
//                        .matchingStatus(OneThingMatchingStatus.COMPLETED)
//                        .matchingType(MatchingType.ONE_THING)
//                        .myOneThingContent("다들 면접 준비는 어떻게 하시나요?")
//                        .restaurantName("신촌 돈까스 전문점")
//                        .location("서대문구 신촌로 234-12")
//                        .menuCategory("일식")
//                        .jobInfos(List.of(
//                                MatchingNoticeDto.JobInfo.builder()
//                                        .jobName("교사")
//                                        .count(1)
//                                        .build(),
//                                MatchingNoticeDto.JobInfo.builder()
//                                        .jobName("간호사")
//                                        .count(2)
//                                        .build(),
//                                MatchingNoticeDto.JobInfo.builder()
//                                        .jobName("공무원")
//                                        .count(1)
//                                        .build()
//                        ))
//                        .dietaryList(List.of("비건","글루텐프리"))
//                        .build()
//        );
    }

}
