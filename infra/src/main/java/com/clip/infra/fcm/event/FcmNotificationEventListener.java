package com.clip.infra.fcm.event;

import com.clip.infra.fcm.service.SendFCMService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;


@Component
@RequiredArgsConstructor
public class FcmNotificationEventListener {
    private final SendFCMService sendFCMService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void sendGeneralFCM(FcmNotificationEvent.GeneralFcmBatchSendEvent fcmEvent) {
        switch (fcmEvent.getMessageTemplateType()) {
            case MATCHING_COMPLETED -> sendFCMService.sendMatchingCompletedMsg(fcmEvent);
            case MATCHING_INFO_OPENED -> sendFCMService.sendMatchingInfoOpenedMsg(fcmEvent);
            case MATCHING_TOMORROW -> sendFCMService.sendMatchingTomorrowMsg(fcmEvent);
            case MATCHING_TODAY -> sendFCMService.sendMatchingTodayMsg(fcmEvent);
            case MATCHING_ENDED -> sendFCMService.sendMatchingFinishedMsg(fcmEvent);
            case MATCHING_STARTED -> sendFCMService.sendMatchingStartedMsg(fcmEvent);
            case MATCHING_MEETUP -> sendFCMService.sendMatchingMeetupMsg(fcmEvent);
        }
    }
}
