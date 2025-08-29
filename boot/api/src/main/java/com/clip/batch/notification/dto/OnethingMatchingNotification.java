package com.clip.batch.notification.dto;

import java.time.LocalDateTime;

public record OnethingMatchingNotification(
        Long userId,
        String fcmToken,
        String deviceType,
        Long oneThingMatchingId,
        LocalDateTime dateTime,
        String place,
        String message
){
}
