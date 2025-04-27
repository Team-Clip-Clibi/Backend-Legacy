package com.clip.infra.fcm.service;

import com.clip.infra.fcm.event.FcmNotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendFCMService {
    private final FCMMsgGenerator fcmMsgGenerator;
    private final FCMMsgSender fcmMsgSender;

    public void sendMatchingMeetupMsg(FcmNotificationEvent.GeneralFcmBatchEvent fcmEvent) {
        fcmMsgSender.send(fcmMsgGenerator.generateGeneralMsg(fcmEvent));
    }

    public void sendMatchingCompletedMsg(FcmNotificationEvent.GeneralFcmBatchEvent fcmEvent) {
        fcmMsgSender.send(fcmMsgGenerator.generateGeneralMsg(fcmEvent));
    }
    public void sendMatchingInfoOpenedMsg(FcmNotificationEvent.GeneralFcmBatchEvent fcmEvent) {
        fcmMsgSender.send(fcmMsgGenerator.generateGeneralMsg(fcmEvent));
    }

    public void sendMatchingTomorrowMsg(FcmNotificationEvent.GeneralFcmBatchEvent fcmEvent) {
        fcmMsgSender.send(fcmMsgGenerator.generateGeneralMsg(fcmEvent));
    }

    public void sendMatchingTodayMsg(FcmNotificationEvent.GeneralFcmBatchEvent fcmEvent) {
        fcmMsgSender.send(fcmMsgGenerator.generateGeneralMsg(fcmEvent));
    }

    public void sendMatchingFinishedMsg(FcmNotificationEvent.GeneralFcmBatchEvent fcmEvent) {
        fcmMsgSender.send(fcmMsgGenerator.generateGeneralMsg(fcmEvent));
    }

    public void sendMatchingStartedMsg(FcmNotificationEvent.GeneralFcmBatchEvent fcmEvent) {
        fcmMsgSender.send(fcmMsgGenerator.generateGeneralMsg(fcmEvent));
    }

}
