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
        List<Notification> notifications = new ArrayList<>();
        List<FcmNotificationEvent.UserFcmData> fcmDataList = new ArrayList<>();

        for (UserOneThingMatching item : items) {
            // FCM 데이터 준비
            User user = item.getUser();
            String token = user.getFirebaseToken();
            String dayOfWeek = item.getOneThingMatching().getMeetingTime().getDayOfWeek().name();
            String time = String.valueOf(item.getOneThingMatching().getMeetingTime().getHour());
            String deviceType = user.getDeviceType().name();

            // MessageParams 객체 생성
            MessageParams.DayOfWeekAndTimeParams messageParams =
                    new MessageParams.DayOfWeekAndTimeParams(dayOfWeek, time);

            // NotificationType을 사용하여 메시지 생성
            String message = templateType.generateMessage(messageParams);

            notifications.add(new Notification(
                    NotificationType.MEETING,
                    false,
                    message,
                    user
            ));

            fcmDataList.add(new FcmNotificationEvent.UserFcmData(
                    item.getOneThingMatching().getId(),
                    deviceType,
                    token,
                    messageParams
            ));
        }

        sendNotificationsAndPublishEvent(notifications, fcmDataList, templateType, "ONE_THING");
    }

    public void processRandomMatchingNotifications(
            List<UserRandomMatching> items,
            MessageTemplateType templateType
    ) {
        if (items.isEmpty()) return;

        List<Notification> notifications = new ArrayList<>();
        List<FcmNotificationEvent.UserFcmData> fcmDataList = new ArrayList<>();

        for (UserRandomMatching item : items) {
            // FCM 데이터 준비
            User user = item.getUser();
            String token = user.getFirebaseToken();
            String dayOfWeek = item.getRandomMatching().getMeetingTime().getDayOfWeek().name();
            String time = String.valueOf(item.getRandomMatching().getMeetingTime().getHour());
            String deviceType = user.getDeviceType().name();

            // MessageParams 객체 생성
            MessageParams.DayOfWeekAndTimeParams messageParams =
                    new MessageParams.DayOfWeekAndTimeParams(dayOfWeek, time);

            // NotificationType을 사용하여 메시지 생성
            String message = templateType.generateMessage(messageParams);

            notifications.add(new Notification(
                    NotificationType.MEETING,
                    false,
                    message,
                    user
            ));

            fcmDataList.add(new FcmNotificationEvent.UserFcmData(
                    item.getRandomMatching().getId(),
                    deviceType,
                    token,
                    messageParams
            ));
        }

        sendNotificationsAndPublishEvent(notifications, fcmDataList, templateType, "RANDOM");
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
