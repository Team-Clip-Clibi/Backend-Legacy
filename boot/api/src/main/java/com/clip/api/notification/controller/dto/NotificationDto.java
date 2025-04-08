package com.clip.api.notification.controller.dto;

import com.clip.notification.entity.NotificationType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class NotificationDto {
    private Long id;
    private NotificationType notificationType;
    private String content;
    private LocalDateTime createdAt;

    @Builder
    public NotificationDto(Long id, NotificationType notificationType, String content, LocalDateTime createdAt) {
        this.id = id;
        this.notificationType = notificationType;
        this.content = content;
        this.createdAt = createdAt;
    }
}
