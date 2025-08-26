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
    public void sendGeneralFCM(FcmNotificationEvent fcmEvent) {
        sendFCMService.sendMsg(fcmEvent);
    }
}
