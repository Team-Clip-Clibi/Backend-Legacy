package com.clip.infra.fcm.service;

import com.clip.infra.fcm.exception.FcmServerException;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;
import org.springframework.retry.annotation.Retryable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
class FCMMsgSender {

    @Retryable(retryFor = FcmServerException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2.0))
    public void send(List<Message> messages) {
        List<Message> currentBatch = new ArrayList<>(messages);
        while (!currentBatch.isEmpty()) {
            try {
                BatchResponse response = FirebaseMessaging.getInstance().sendEach(currentBatch);

                // 실패한 메시지(재시도 가능한 에러만)만 남김
                currentBatch = IntStream.range(0, response.getResponses().size())
                        .filter(i -> isRetryableFailure(response.getResponses().get(i)))
                        .mapToObj(currentBatch::get)
                        .toList();

            } catch (FirebaseMessagingException e) {
                if (isRetryableFailure(e)) {
                    throw new FcmServerException();
                }
                throw new RuntimeException("FCM 메시지 전송 실패", e);
            }
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