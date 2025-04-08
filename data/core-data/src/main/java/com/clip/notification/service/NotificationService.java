package com.clip.notification.service;

import com.clip.notification.entity.Notification;
import com.clip.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
        return notificationRepository.findUnreadNotifications(userId, lastId, LocalDateTime.now().minusWeeks(2),PageRequest.ofSize(PAGE_SIZE));
    }

    public List<Notification> findReadNotifications(long userId, Long lastId) {
        return notificationRepository.findReadNotifications(userId, lastId, LocalDateTime.now().minusWeeks(2), PageRequest.ofSize(PAGE_SIZE));
    }

    public void updateToRead(long userId, long notificationId) {
        notificationRepository.updateToRead(userId, notificationId);
    }
}
