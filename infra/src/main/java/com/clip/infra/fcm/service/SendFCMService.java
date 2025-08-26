package com.clip.infra.fcm.service;

import com.clip.infra.fcm.event.FcmNotificationEvent;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class SendFCMService {
    private final FCMMsgGenerator fcmMsgGenerator;
    private final FCMMsgSender fcmMsgSender;

    public void sendMsg(FcmNotificationEvent fcmEvent) {
        Map<Long, Message> messageMap = fcmMsgGenerator.generateGeneralMsg(fcmEvent);
        fcmMsgSender.send(messageMap);
    }
}
