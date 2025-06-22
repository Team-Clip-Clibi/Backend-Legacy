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
    public MatchingProgressStatusDto getMatchingStatus(UserDetails userDetails) {
        return userMatchingService.getUserMatchingStatus(Long.parseLong(userDetails.getUsername()));
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

}
