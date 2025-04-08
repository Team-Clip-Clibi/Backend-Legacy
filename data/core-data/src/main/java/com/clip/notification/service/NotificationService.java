package com.clip.notification.service;

import com.clip.notification.entity.Notification;
import com.clip.notification.repository.NotificationRepository;
import com.clip.notification.exception.NotExistNotificationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final int PAGE_SIZE = 50;
    private final NotificationRepository notificationRepository;

    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }

    public List<Notification> findUnreadNotifications(long userId, Long lastId) {
        List<Notification> unreadNotifications = notificationRepository.findUnreadNotifications(userId, lastId, LocalDateTime.now().minusWeeks(2), PageRequest.ofSize(PAGE_SIZE));
        if (unreadNotifications.isEmpty()) {
            throw new NotExistNotificationException();
        }
        return unreadNotifications;
    }

    public List<Notification> findReadNotifications(long userId, Long lastId) {
        List<Notification> readNotifications = notificationRepository.findReadNotifications(userId, lastId, LocalDateTime.now().minusWeeks(2), PageRequest.ofSize(PAGE_SIZE));
        if (readNotifications.isEmpty()) {
            throw new NotExistNotificationException();
        }
        return readNotifications;
    }

    public void updateToRead(long userId, long notificationId) {
        notificationRepository.updateToRead(userId, notificationId);
    }
}
