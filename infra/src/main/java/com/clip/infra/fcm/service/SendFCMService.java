package com.clip.infra.fcm.service;

import com.clip.infra.fcm.event.FcmNotificationEvent;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class SendFCMService {
    private final FCMMsgGenerator fcmMsgGenerator;
    private final FCMMsgSender fcmMsgSender;

    @Async
    public void sendMsg(final MessageTemplateType messageTemplateType, final String matchingType, final Map<Long, FcmNotificationEvent.UserFcmData> userDataMap) {
        Map<Long, Message> messageMap = fcmMsgGenerator.generateGeneralMsg(messageTemplateType, matchingType, userDataMap);
        fcmMsgSender.send(messageMap);
    }
}
