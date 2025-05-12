package com.clip.infra.fcm.event;

import lombok.Getter;

import java.util.List;

@Getter
public class FcmSendFailedEvent {
    private final List<Long> notificationIds;

    public FcmSendFailedEvent(List<Long> notificationIds) {
        this.notificationIds = notificationIds;
    }

}