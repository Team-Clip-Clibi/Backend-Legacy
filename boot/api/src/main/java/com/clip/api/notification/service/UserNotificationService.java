package com.clip.api.notification.service;

import com.clip.api.notification.controller.dto.NotificationDto;
import com.clip.api.notification.mapper.NotificationMapper;
import com.clip.api.notification.service.exception.NotExistNotificationException;
import com.clip.notification.entity.Notification;
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
        List<Notification> unreadNotifications = notificationService.findUnreadNotifications(userId, lastId);
        if (unreadNotifications.isEmpty()) {
            throw new NotExistNotificationException();
        }
        return notificationMapper.toNotificationDto(unreadNotifications);
    }

    public List<NotificationDto> getReadNotifications(long userId, Long lastId) {
        List<Notification> readNotifications = notificationService.findReadNotifications(userId, lastId);
        if (readNotifications.isEmpty()) {
            throw new NotExistNotificationException();
        }
        return notificationMapper.toNotificationDto(readNotifications);
    }

    public void updateToRead(long userId, long notificationId) {
        notificationService.updateToRead(userId, notificationId);
    }
}
