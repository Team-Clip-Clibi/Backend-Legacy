package com.clip.infra.fcm.service;

import com.clip.infra.fcm.event.FcmSendFailedEvent;
import com.clip.infra.fcm.exception.FcmServerException;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.stereotype.Component;
import org.springframework.retry.annotation.Retryable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
class FCMMsgSender {
    private final ApplicationEventPublisher eventPublisher;

    @Retryable(retryFor = FcmServerException.class,
            maxAttempts = 3,
            recover = "saveFailedNotificationIds",
            backoff = @Backoff(delay = 1000, multiplier = 2.0))
    public void send(Map<Long, Message> messageMap) {
        Map<Long, Message> currentBatch = new HashMap<>(messageMap);

        while (!currentBatch.isEmpty()) {
            try {
                List<Message> messages = new ArrayList<>(currentBatch.values());
                List<Long> notificationIds = new ArrayList<>(currentBatch.keySet());

                BatchResponse response = FirebaseMessaging.getInstance().sendEach(messages);

                // 실패한 메시지(재시도 가능한 에러만)만 남김
                Map<Long, Message> nextBatch = new HashMap<>();
                for (int i = 0; i < response.getResponses().size(); i++) {
                    if (isRetryableFailure(response.getResponses().get(i))) {
                        Long notificationId = notificationIds.get(i);
                        nextBatch.put(notificationId, currentBatch.get(notificationId));
                    }
                }
                currentBatch = nextBatch;

            } catch (FirebaseMessagingException e) {
                if (isRetryableFailure(e)) {
                    throw new FcmServerException(new HashMap<>(currentBatch));
                }
                log.error("FCM 메시지 전송 실패: {}", e.getMessage(), e);
                return;
            }
        }
    }

    /**
     * 최대 재시도 후에도 실패한 메시지가 남아있을 경우
     */
    @Recover
    public void saveFailedNotificationIds(FcmServerException e) {
        Map<Long, Message> failedBatch = e.getFailedBatch();
        if (failedBatch != null && !failedBatch.isEmpty()) {
            List<Long> failedNotificationIds = new ArrayList<>(failedBatch.keySet());
            eventPublisher.publishEvent(new FcmSendFailedEvent(failedNotificationIds));
            log.error("FCM 메시지 최종 전송 실패. 실패한 notificationIds: {}", failedNotificationIds);
        }
    }

    private boolean isRetryableFailure(SendResponse response) {
        return !response.isSuccessful()
                && response.getException() != null
                && List.of(MessagingErrorCode.INTERNAL, MessagingErrorCode.UNAVAILABLE)
                .contains(response.getException().getMessagingErrorCode());
    }

    private boolean isRetryableFailure(FirebaseMessagingException e) {
        return List.of(MessagingErrorCode.INTERNAL, MessagingErrorCode.UNAVAILABLE)
                .contains(e.getMessagingErrorCode());
    }
}