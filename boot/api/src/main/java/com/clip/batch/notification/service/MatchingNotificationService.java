package com.clip.batch.notification.service;

import com.clip.infra.fcm.event.FcmNotificationEvent;
import com.clip.infra.fcm.service.MessageParams;
import com.clip.infra.fcm.service.MessageTemplateType;
import com.clip.matching.entity.UserOneThingMatching;
import com.clip.matching.entity.UserRandomMatching;
import com.clip.notification.entity.Notification;
import com.clip.notification.entity.NotificationType;
import com.clip.notification.service.NotificationService;
import com.clip.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class MatchingNotificationService {
    private final NotificationService notificationService;
    private final ApplicationEventPublisher sendFCMEventPublisher;

    public void processOneThingMatchingNotifications(
            List<UserOneThingMatching> items,
            MessageTemplateType templateType
    ) {
        processMatchingNotifications(
                items,
                templateType,
                "ONE_THING",
                item -> item.getUser(),
                item -> item.getOneThingMatching().getMeetingTime().getDayOfWeek().name(),
                item -> String.valueOf(item.getOneThingMatching().getMeetingTime().getHour()),
                item -> item.getOneThingMatching().getId()
        );
    }

    public void processRandomMatchingNotifications(
            List<UserRandomMatching> items,
            MessageTemplateType templateType
    ) {
        processMatchingNotifications(
                items,
                templateType,
                "RANDOM",
                item -> item.getUser(),
                item -> item.getRandomMatching().getMeetingTime().getDayOfWeek().name(),
                item -> String.valueOf(item.getRandomMatching().getMeetingTime().getHour()),
                item -> item.getRandomMatching().getId()
        );
    }

    private <T> void processMatchingNotifications(
            List<T> items,
            MessageTemplateType templateType,
            String matchingType,
            Function<T, User> userExtractor,
            Function<T, String> dayOfWeekExtractor,
            Function<T, String> timeExtractor,
            Function<T, Long> matchingIdExtractor
    ) {
        List<Notification> notifications = new ArrayList<>();
        List<FcmNotificationEvent.UserFcmData> fcmDataList = new ArrayList<>();

        for (T item : items) {
            // FCM 데이터 준비
            User user = userExtractor.apply(item);
            String token = user.getFirebaseToken();
            String dayOfWeek = dayOfWeekExtractor.apply(item);
            String time = timeExtractor.apply(item);
            String deviceType = user.getDeviceType().name();

            // MessageParams 객체 생성
            MessageParams.DayOfWeekAndTimeParams messageParams =
                    new MessageParams.DayOfWeekAndTimeParams(dayOfWeek, time);

            // 메시지 생성
            String message = templateType.generateMessage(messageParams);

            notifications.add(new Notification(
                    NotificationType.MEETING,
                    false,
                    message,
                    user
            ));

            fcmDataList.add(new FcmNotificationEvent.UserFcmData(
                    matchingIdExtractor.apply(item),
                    deviceType,
                    token,
                    messageParams
            ));
        }

        sendNotificationsAndPublishEvent(notifications, fcmDataList, templateType, matchingType);
    }

    private void sendNotificationsAndPublishEvent(
            List<Notification> notifications,
            List<FcmNotificationEvent.UserFcmData> fcmDataList,
            MessageTemplateType templateType,
            String matchingType
    ) {
        // 알림 저장
        List<Notification> savedNotifications = notificationService.saveNotifications(notifications);

        // 저장된 알림 ID와 FCM 데이터 매핑
        Map<Long, FcmNotificationEvent.UserFcmData> userDataMap = new HashMap<>();
        IntStream.range(0, savedNotifications.size())
                .forEach(i -> userDataMap.put(savedNotifications.get(i).getId(), fcmDataList.get(i)));

        // FCM 이벤트 발행
        if (!userDataMap.isEmpty()) {
            sendFCMEventPublisher.publishEvent(new FcmNotificationEvent.GeneralFcmBatchSendEvent(
                    this,
                    templateType,
                    matchingType,
                    userDataMap));
        }
    }
}