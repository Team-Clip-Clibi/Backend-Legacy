package com.clip.infra.fcm.service;

import com.clip.infra.fcm.event.FcmNotificationEvent;
import com.google.firebase.messaging.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
class FCMMsgGenerator {
    private static final String TITLE = "OneThing";

    public List<Message> generateGeneralMsg(final FcmNotificationEvent.GeneralFcmBatchEvent fcmEvent) {
        List<Message> messages = new ArrayList<>();

        // userDataMap의 각 항목에 대해 메시지 생성
        for (Map.Entry<Long, FcmNotificationEvent.UserFcmData> entry : fcmEvent.getUserDataMap().entrySet()) {
            Long notificationId = entry.getKey();
            FcmNotificationEvent.UserFcmData userData = entry.getValue();

            Message message = generateMessage(
                    notificationId,
                    fcmEvent.getMessageTemplateType(),
                    fcmEvent.getMatchingType(),
                    userData
            );

            messages.add(message);
        }

        return messages;
    }

    private Message generateMessage(
            Long notificationId,
            MessageTemplateType messageTemplateType,
            String matchingType,
            FcmNotificationEvent.UserFcmData userData) {

        // 데이터 맵 생성
        HashMap<String, String> data = generateGeneralFcmData(
                notificationId,
                messageTemplateType,
                userData.matchingId(),
                matchingType);

        // 알림 객체 생성
        Notification notification = Notification.builder()
                .setTitle(TITLE)
                .setBody(generateGeneralMsgBody(
                        userData.params(),
                        messageTemplateType))
                .build();

        // 메시지 빌더 생성 및 기본 설정
        Message.Builder msgBuilder = Message.builder()
                .setNotification(notification)
                .putAllData(data)
                .setToken(userData.fcmToken());

        // 디바이스 타입에 따른 설정
        if (!userData.targetDeviceType().equals("iOS")) {
            msgBuilder.setAndroidConfig(
                    AndroidConfig.builder()
                            .setNotification(AndroidNotification.builder()
                                    .setClickAction(messageTemplateType.name())
                                    .build())
                            .build()
            );
        }

        return msgBuilder.build();
    }

    private HashMap<String, String> generateGeneralFcmData(Long notificationId, MessageTemplateType messageTemplateType, Long matchingId, String matchingType) {
        HashMap<String, String> data = new HashMap<>();
        data.put("notificationId", notificationId.toString());
        data.put("notificationType", messageTemplateType.name());
        data.put("matchingId", matchingId.toString());
        data.put("matchingType", matchingType);
        return data;
    }

    private String generateGeneralMsgBody(Object data, MessageTemplateType messageTemplateType) {
        if (!(data instanceof MessageParams params)) {
            throw new IllegalArgumentException("Invalid data type for FCM message: " + data);
        }
        return messageTemplateType.generateMessage(params);
    }
}

