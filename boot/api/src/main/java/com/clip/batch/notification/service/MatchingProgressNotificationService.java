package com.clip.batch.notification.service;

import com.clip.batch.matching.service.SaveNotificationService;
import com.clip.infra.fcm.event.FcmNotificationEvent;
import com.clip.infra.fcm.service.MessageParams;
import com.clip.infra.fcm.service.MessageTemplateType;
import com.clip.matching.entity.UserOneThingMatching;
import com.clip.matching.entity.UserRandomMatching;
import com.clip.notification.entity.Notification;
import com.clip.notification.entity.NotificationBanner;
import com.clip.notification.entity.NotificationBannerType;
import com.clip.notification.entity.NotificationType;
import com.clip.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class MatchingProgressNotificationService {
    private final SaveNotificationService saveNotificationService;
    private final ApplicationEventPublisher sendFCMEventPublisher;

    // 원띵 매칭 처리
    public void processOneThingProgressNotifications(
            List<UserOneThingMatching> items,
            MessageTemplateType templateType
    ) {
        // 그룹화 및 알림 처리
        processMatchingNotifications(
                items,
                templateType,
                "ONE_THING",
                item -> item.getOneThingMatching().getId(),
                UserOneThingMatching::getUser
        );
    }

    // 랜덤 매칭 처리
    public void processRandomProgressNotifications(
            List<UserRandomMatching> items,
            MessageTemplateType templateType
    ) {
        // 그룹화 및 알림 처리
        processMatchingNotifications(
                items,
                templateType,
                "RANDOM",
                item -> item.getRandomMatching().getId(),
                UserRandomMatching::getUser
        );
    }

    private <T> void processMatchingNotifications(
            List<T> items,
            MessageTemplateType templateType,
            String matchingType,
            Function<T, Long> matchingIdExtractor,
            Function<T, User> userExtractor
    ) {
        // 매칭 ID 기준으로 그룹화
        Map<Long, List<T>> groupedByMatchingId = items.stream()
                .collect(Collectors.groupingBy(matchingIdExtractor));

        // 모든 알림과 FCM 데이터를 저장할 리스트
        List<Notification> allNotifications = new ArrayList<>();
        List<NotificationBanner> allNotificationBanners = new ArrayList<>();
        List<FcmNotificationEvent.UserFcmData> allFcmDataList = new ArrayList<>();

        // 각 그룹별로 처리
        for (Map.Entry<Long, List<T>> entry : groupedByMatchingId.entrySet()) {
            Long matchingId = entry.getKey();
            List<T> matchingUsers = entry.getValue();

            // 각 그룹별 알림 객체와 FCM 데이터 준비
            List<Notification> groupNotifications = new ArrayList<>();
            List<NotificationBanner> groupNotificationBanners = new ArrayList<>();
            List<FcmNotificationEvent.UserFcmData> groupFcmDataList = new ArrayList<>();

            // 랜덤 닉네임 선택 (MATCHING_STARTED 타입인 경우)
            String randomNickname = null;
            if (templateType == MessageTemplateType.MATCHING_STARTED && !matchingUsers.isEmpty()) {
                int randomIndex = (int) (Math.random() * matchingUsers.size());
                User randomUser = userExtractor.apply(matchingUsers.get(randomIndex));
                randomNickname = randomUser.getNickname();
            }

            for (T item : matchingUsers) {
                User user = userExtractor.apply(item);
                String token = user.getFirebaseToken();
                String nickname = templateType == MessageTemplateType.MATCHING_STARTED
                        ? randomNickname  // MATCHING_STARTED일 경우 랜덤 선택된 닉네임 사용
                        : user.getNickname();  // 그 외의 경우 각 사용자의 닉네임 사용
                String deviceType = user.getDeviceType().name();

                // 메시지 생성
                MessageParams.NicknameParams messageParams = new MessageParams.NicknameParams(nickname);
                String message = templateType.generateMessage(messageParams);

                // 알림 객체 생성
                groupNotifications.add(new Notification(
                        NotificationType.MEETING,
                        false,
                        message,
                        user,
                        0L,
                        "ONETHING"

                ));

                if (templateType.equals(MessageTemplateType.MATCHING_ENDED)) {
                    groupNotificationBanners.add(new NotificationBanner(
                            user,
                            NotificationBannerType.REVIEW,
                            false
                    ));
                }

                groupFcmDataList.add(new FcmNotificationEvent.UserFcmData(
                        matchingId,
                        deviceType,
                        token,
                        messageParams
                ));
            }

            // 그룹별 데이터를 전체 리스트에 추가
            allNotifications.addAll(groupNotifications);
            allNotificationBanners.addAll(groupNotificationBanners);
            allFcmDataList.addAll(groupFcmDataList);
        }

        sendNotificationsAndPublishEvent(allNotifications, allNotificationBanners, allFcmDataList, templateType, matchingType);
    }

    private void sendNotificationsAndPublishEvent(
            List<Notification> notifications,
            List<NotificationBanner> notificationBanners,
            List<FcmNotificationEvent.UserFcmData> fcmDataList,
            MessageTemplateType templateType,
            String matchingType
    ) {
        if (notifications.isEmpty()) return;

        // 알림 저장
        List<Notification> savedNotifications = saveNotificationService.saveNotifications(notifications);

        if (templateType.equals(MessageTemplateType.MATCHING_ENDED)) {
            saveNotificationService.saveNotificationBanners(notificationBanners);
        }

        // 저장된 알림 ID와 FCM 데이터 매핑
        Map<Long, FcmNotificationEvent.UserFcmData> userDataMap = new HashMap<>();
        IntStream.range(0, savedNotifications.size())
                .forEach(i -> userDataMap.put(savedNotifications.get(i).getId(), fcmDataList.get(i)));

        // FCM 이벤트 발행
        if (!userDataMap.isEmpty()) {
            sendFCMEventPublisher.publishEvent(new FcmNotificationEvent(
                    this,
                    templateType,
                    matchingType,
                    userDataMap));
        }
    }
}

