package com.clip.infra.fcm.event;

import com.clip.infra.fcm.service.SendFCMService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class FcmNotificationEventListener {
    private final SendFCMService sendFCMService;

    @Async
    @EventListener
    public void sendGeneralFCM(FcmNotificationEvent fcmEvent) {
        sendFCMService.sendMsg(fcmEvent.getMessageTemplateType(), fcmEvent.getMatchingType(), fcmEvent.getUserDataMap());
    }
}
