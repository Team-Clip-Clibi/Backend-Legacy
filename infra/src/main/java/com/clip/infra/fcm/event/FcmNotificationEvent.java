package com.clip.infra.fcm.event;


import com.clip.infra.fcm.service.MessageParams;
import com.clip.infra.fcm.service.MessageTemplateType;
import lombok.Getter;
import lombok.Builder;
import org.springframework.context.ApplicationEvent;

import java.util.Map;

public class FcmNotificationEvent {

    // 공통 FCM 이벤트 베이스 클래스
    @Getter
    public static abstract class CommonFcmEvent extends ApplicationEvent {
        private final Long notificationId;
        private final String targetDeviceType;
        private final MessageTemplateType messageTemplateType;

        public CommonFcmEvent(Object source, Long notificationId, String targetDeviceType, MessageTemplateType notificationType) {
            super(source);
            this.notificationId = notificationId;
            this.targetDeviceType = targetDeviceType;
            this.messageTemplateType = notificationType;
        }
    }

    // 일반 사용자 대상 배치 FCM 이벤트 (매칭 관련 알림 등)
    @Getter
    public static class GeneralFcmMultiSendEvent extends ApplicationEvent {
        private final MessageTemplateType messageTemplateType;
        private final String matchingType;
        private final Map<Long, UserFcmData> userDataMap;

        @Builder
        public GeneralFcmMultiSendEvent(
                Object source,
                MessageTemplateType messageTemplateType,
                String matchingType,
                Map<Long, UserFcmData> userDataMap
        ) {
            super(source);
            this.messageTemplateType = messageTemplateType;
            this.matchingType = matchingType;
            this.userDataMap = userDataMap;

            if (!messageTemplateType.isGeneral()) {
                throw new IllegalArgumentException("NotificationType must be general type");
            }
        }
    }

    public record UserFcmData(
            Long matchingId,
            String targetDeviceType,
            String fcmToken,
            MessageParams params
    ) {}

}