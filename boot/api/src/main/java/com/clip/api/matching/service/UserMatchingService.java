package com.clip.api.matching.service;

import com.clip.api.matching.controller.dto.*;
import com.clip.api.matching.mapper.MatchingMapper;
import com.clip.infra.fcm.event.FcmNotificationEvent;
import com.clip.infra.fcm.service.MessageParams;
import com.clip.infra.fcm.service.MessageTemplateType;
import com.clip.matching.entity.*;
import com.clip.matching.service.MatchingService;
import com.clip.notification.entity.Notification;
import com.clip.notification.entity.NotificationType;
import com.clip.notification.service.NotificationService;
import com.clip.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final ApplicationEventPublisher sendFCMEventPublisher;
    private final NotificationService notificationService;


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

    @Transactional(readOnly = true)
    public MatchingProgressInfoDto getUserMatchingProgressInfo(MatchingType matchingType, long matchingId, long userId) {

        switch (matchingType) {
            case RANDOM -> {
                Long userRandomMatchingId = matchingService.findUserRandomMatchingNotEndedStatus(matchingId, userId)
                        .getRandomMatching()
                        .getId();

                List<UserRandomMatching> allUserRandomMatchings = matchingService.findAllUserRandomMatchings(userRandomMatchingId);

                return getRandomMatchingProgressStatusDto(allUserRandomMatchings);
            }

            case ONE_THING -> {
                Long userOneThingMatchingId = matchingService.findUserOnethingMatchingNotEndedStatus(matchingId, userId)
                        .getOneThingMatching()
                        .getId();

                List<UserOneThingMatching> allUserOneThingMatchings = matchingService.findAllUserOneThingMatchings(userOneThingMatchingId);

                return getOneThingMatchingProgressStatusDto(allUserOneThingMatchings);
            }

            default -> throw new IllegalStateException("지원하지 않는 매칭 타입입니다.: " + matchingType);
        }
    }

    private static MatchingProgressInfoDto getRandomMatchingProgressStatusDto(
            List<UserRandomMatching> myRandomMatchingGroup
    ) {

        List<String> shuffledNicknames = getShuffledResult(
                myRandomMatchingGroup,
                oneThing -> oneThing.getUser().getNickname()
        );

        List<String> shuffledTmi = getShuffledResult(
                myRandomMatchingGroup,
                UserRandomMatching::getTmi
        );

        Map<String, String> nicknameOneThingContentMap = myRandomMatchingGroup.stream()
                .collect(Collectors.toMap(
                        userRandom -> userRandom.getUser().getNickname(),
                        UserRandomMatching::getOnethingTopic)
                );

        return MatchingProgressInfoDto.builder()
                .nicknameList(shuffledNicknames)
                .tmiList(shuffledTmi)
                .nicknameOnethingMap(nicknameOneThingContentMap)
                .build();
    }

    private static MatchingProgressInfoDto getOneThingMatchingProgressStatusDto(
            List<UserOneThingMatching> myOneThingMatchingGroup
    ) {

        List<String> shuffledNicknames = getShuffledResult(
                myOneThingMatchingGroup,
                oneThing -> oneThing.getUser().getNickname()
        );

        List<String> shuffledTmi = getShuffledResult(
                myOneThingMatchingGroup,
                UserOneThingMatching::getTmi
        );

        Map<String, String> nicknameOneThingContentMap = myOneThingMatchingGroup.stream()
                .collect(Collectors.toMap(
                        userOneThing -> userOneThing.getUser().getNickname(),
                        UserOneThingMatching::getOnethingTopic)
                );

        return MatchingProgressInfoDto.builder()
                .nicknameList(shuffledNicknames)
                .tmiList(shuffledTmi)
                .nicknameOnethingMap(nicknameOneThingContentMap)
                .build();
    }

    private static <T, R> List<R> getShuffledResult(
            List<T> targetList,
            Function<T, R> getElementFunc
    ) {
        return targetList.stream()
                .map(getElementFunc)
                .collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
                    Collections.shuffle(list);
                    return list;
                }));
    }

    public void updateMatchingStatusChecked(long userId, MatchingType matchingType, long matchingId) {
        switch (matchingType) {
            case ONE_THING -> matchingService.updateUserOneThingMatchingStatusToEnded(userId, matchingId);
            case RANDOM -> matchingService.updateUserRandomMatchingStatusToEnded(userId, matchingId);
        }
    }

    public MatchingOverviewDto getMatchingOverview(Long userId) {
        // 매칭 신청한 매칭 수와 확정된 매칭 수 계산
        long appliedMatchingCount =
                matchingService.findAllAppliedOneThingMatching(userId, OneThingMatchingStatus.APPLIED).size() +
                        matchingService.findAllAppliedRandomMatching(userId, RandomMatchingStatus.APPLIED).size();

        List<UserOneThingMatching> confirmedOneThingMatchings =
                matchingService.findAllConfirmedOneThingMatching(userId, OneThingMatchingStatus.CONFIRMED);
        List<UserRandomMatching> confirmedRandomMatchings =
                matchingService.findAllConfirmedRandomMatching(userId, RandomMatchingStatus.CONFIRMED);
        long confirmedMatchingCount = confirmedOneThingMatchings.size() + confirmedRandomMatchings.size();

        // 매칭 안내문 확인 여부
        boolean isAllNoticeRead = (confirmedOneThingMatchings.isEmpty() || confirmedOneThingMatchings.stream().allMatch(UserOneThingMatching::isNoticeRead)) &&
                        (confirmedRandomMatchings.isEmpty() || confirmedRandomMatchings.stream().allMatch(UserRandomMatching::isNoticeRead));

        // 다음 매칭 날짜 계산
        Optional<LocalDate> oneThingDate = matchingService.findOptLatestUserOneThingMatchingNotEndedStatus(userId, LocalDateTime.now())
                .map(match -> match.getOneThingMatching().getDateTime().toLocalDate());

        Optional<LocalDate> randomDate = matchingService.findOptLatestUserRandomMatchingNotEndedStatus(userId, LocalDateTime.now())
                .map(match -> match.getRandomMatching().getDateTime().toLocalDate());

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

    public List<MatchingDto> getMatchings(RandomMatchingStatus matchingStatus, LocalDateTime lastMeetingTime, long userId) {
        return matchingMapper.toDto(matchingService.findAllMatchings(matchingStatus, lastMeetingTime, userId));
    }

    public MatchingDetailDto getMatchingDetail(long userId, MatchingType matchingType, long id) {
        switch (matchingType) {
            case ONE_THING -> {
                UserOneThingMatching  matchingInfo = matchingService.findUserOneThingMatchingWithMatchingInfo(id);
                UserOneThingMatching  paymentInfo = matchingService.findUserOneThingMatchingWithPaymentInfo(id);
                return matchingMapper.toOneThingMatchingDetailDto(
                        matchingInfo, paymentInfo);
            }
            case RANDOM -> {
                return matchingMapper.toRandomMatchingDetailDto(
                        matchingService.findUserRandomMatchingWithFetch(id));
            }
            default -> throw new IllegalArgumentException("Invalid Matching Type: " + matchingType);
        }
    }

    public void cancelMatching(long userId, MatchingType matchingType, long id) {
        switch (matchingType) {
            case ONE_THING -> matchingService.cancelUserOneThingMatching(id);
            case RANDOM -> matchingService.cancelUserRandomMatching(id);
            default -> throw new IllegalArgumentException("Invalid Matching Type: " + matchingType);
        }
    }

    @Transactional
    public void updateLastMinutesAndSendNotification(long userId, MatchingType matchingType, long id, int lateMinutes) {
        switch (matchingType) {
            case ONE_THING -> {
                    UserOneThingMatching lateComer = matchingService.findUserOneThingMatching(id);
                    lateComer.updateLateMinutes(lateMinutes);
                    List<UserOneThingMatching> oneThingMatchingParticipants = matchingService.findAllUserOneThingMatchingsForNotification(lateComer.getOneThingMatching().getId());
                    List<User> participants = oneThingMatchingParticipants.stream()
                            .map(UserOneThingMatching::getUser)
                            .toList();
                    sendFcmMessageAndSaveNotification(participants, lateComer.getUser(), lateMinutes, lateComer.getOneThingMatching().getId(), matchingType.name());
            }
            case RANDOM -> {
                    UserRandomMatching lateComer = matchingService.findUserRandomMatching(id);
                    lateComer.updateLateMinutes(lateMinutes);
                    List<UserRandomMatching> randomMatchingParticipants = matchingService.findAllUserRandomMatchingsForNotification(lateComer.getRandomMatching().getId());
                    List<User> participants = randomMatchingParticipants.stream()
                            .map(UserRandomMatching::getUser)
                            .toList();
                    sendFcmMessageAndSaveNotification(participants, lateComer.getUser(), lateMinutes, lateComer.getRandomMatching().getId(), matchingType.name());
            }
            default -> throw new IllegalArgumentException("Invalid Matching Type: " + matchingType);
        }
    }

    private void sendFcmMessageAndSaveNotification(List<User> participants, User lateComer, int lateMinutes, long matchingId, String matchingType) {
        List<User> targetUsers = participants.stream()
                .filter(user -> !user.equals(lateComer))
                .toList();

        if (targetUsers.isEmpty()) {
            return;
        }

        // 메시지 생성
        MessageParams.NicknameAndTimeParams messageParams =
                new MessageParams.NicknameAndTimeParams(lateComer.getNickname(), lateMinutes);
        String message = MessageTemplateType.LATE_ARRIVAL.generateMessage(messageParams);

        // 알림 저장
        List<Notification> notifications = targetUsers.stream()
                .map(user -> Notification.builder()
                        .user(user)
                        .notificationType(NotificationType.MEETING)
                        .content(message)
                        .build())
                .toList();

        List<Notification> savedNotifications = notificationService.saveAll(notifications);

        // FCM 데이터와 알림 ID 매핑
        Map<Long, FcmNotificationEvent.UserFcmData> userDataMap = new HashMap<>();
        for (int i = 0; i < savedNotifications.size(); i++) {
            userDataMap.put(
                    savedNotifications.get(i).getId(),
                    new FcmNotificationEvent.UserFcmData(
                            matchingId,
                            targetUsers.get(i).getDeviceType().name(),
                            targetUsers.get(i).getFirebaseToken(),
                            messageParams
                    )
            );
        }

        // FCM 이벤트 발행
        sendFCMEventPublisher.publishEvent(new FcmNotificationEvent.GeneralFcmMultiSendEvent(
                this,
                MessageTemplateType.LATE_ARRIVAL,
                matchingType,
                userDataMap
        ));
    }
}
