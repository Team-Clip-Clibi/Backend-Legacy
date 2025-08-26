package com.clip.batch.notification.projection;

import com.clip.user.entity.DeviceType;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

public record OnethingMatchingProjection(
        Long userId,
        String fcmToken,
        String deviceType,
        Long oneThingMatchingId,
        DayOfWeek dayOfWeek,
        LocalDateTime dateTime
) {
}