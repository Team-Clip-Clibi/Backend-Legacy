package com.clip.infra.fcm.exception;

import com.google.firebase.messaging.Message;
import java.util.Map;

public class FcmServerException extends RuntimeException {
    private final Map<Long, Message> failedBatch;

    public FcmServerException(Map<Long, Message> failedBatch) {
        this.failedBatch = failedBatch;
    }

    public Map<Long, Message> getFailedBatch() {
        return failedBatch;
    }
}
