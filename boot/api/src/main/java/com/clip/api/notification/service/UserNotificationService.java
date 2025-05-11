package com.clip.api.notification.service;

import com.clip.api.notification.controller.dto.NotificationBannerDto;
import com.clip.api.notification.controller.dto.NotificationDto;
import com.clip.api.notification.mapper.NotificationMapper;
import com.clip.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserNotificationService {
    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;

    public List<NotificationDto> getUnreadNotifications(long userId, Long lastId) {
        return notificationMapper.toNotificationDto(
                notificationService.findUnreadNotifications(userId, lastId)
        );
    }

    public List<NotificationDto> getReadNotifications(long userId, Long lastId) {
        return notificationMapper.toNotificationDto(
                notificationService.findReadNotifications(userId, lastId)
        );
    }

    public void updateToRead(long userId, long notificationId) {
        notificationService.updateToRead(userId, notificationId);
    }

    public List<NotificationBannerDto> getNotificationBanners(long userId) {
        return notificationMapper.toNotificationBannerDto(
                notificationService.findNotificationBanners(userId)
        );
    }

    public void updateToClosed(long userId, long notificationBannerId) {
        notificationService.updateToClosed(userId, notificationBannerId);
    }
}
