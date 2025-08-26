package com.clip.infra.fcm.event;


import com.clip.infra.fcm.service.MessageParams;
import com.clip.infra.fcm.service.MessageTemplateType;
import lombok.Getter;
import lombok.Builder;
import org.springframework.context.ApplicationEvent;

import java.util.Map;

@Getter
public class FcmNotificationEvent extends ApplicationEvent {
    private final MessageTemplateType messageTemplateType;
    private final String matchingType;
    private final Map<Long, UserFcmData> userDataMap;

    public FcmNotificationEvent(Object source,
                                MessageTemplateType messageTemplateType,
                                String matchingType,
                                Map<Long, UserFcmData> userDataMap) {
        super(source);
        this.messageTemplateType = messageTemplateType;
        this.matchingType = matchingType;
        this.userDataMap = userDataMap;

        if (!messageTemplateType.isGeneral()) {
            throw new IllegalArgumentException("NotificationType must be general type");
        }
    }


    public record UserFcmData(
            Long matchingId,
            String targetDeviceType,
            String fcmToken,
            MessageParams params
    ) {
    }

}