package com.clip.api.notification.controller.dto;

import com.clip.notification.entity.NotificationBannerType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NotificationBannerDto {
    private Long id;
    private NotificationBannerType notificationBannerType;

    @Builder
    public NotificationBannerDto(Long id, NotificationBannerType notificationBannerType) {
        this.id = id;
        this.notificationBannerType = notificationBannerType;
    }
}
