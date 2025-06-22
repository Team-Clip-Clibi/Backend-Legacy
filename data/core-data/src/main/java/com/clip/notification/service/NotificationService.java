package com.clip.notification.service;

import com.clip.global.exception.NoContentAvailableException;
import com.clip.notification.entity.Notification;
import com.clip.notification.entity.NotificationBanner;
import com.clip.notification.repository.NotificationBannerRepository;
import com.clip.notification.repository.NotificationRepository;
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
    private final NotificationBannerRepository notificationBannerRepository;

    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }

    public List<Notification> saveAll(List<Notification> notifications) {
        return notificationRepository.saveAll(notifications);
    }

    public List<Notification> findUnreadNotifications(long userId, Long lastId) {
        List<Notification> unreadNotifications = notificationRepository.findUnreadNotifications(
                userId,
                lastId,
                LocalDateTime.now().minusWeeks(2),
                PageRequest.ofSize(PAGE_SIZE)
        );
        if (unreadNotifications.isEmpty()) {
            throw new NoContentAvailableException("unreadNotifications", lastId);
        }
        return unreadNotifications;
    }

    public List<Notification> findReadNotifications(long userId, Long lastId) {
        List<Notification> readNotifications = notificationRepository.findReadNotifications(
                userId,
                lastId,
                LocalDateTime.now().minusWeeks(2),
                LocalDateTime.now().minusDays(30),
                PageRequest.ofSize(PAGE_SIZE)
        );
        if (readNotifications.isEmpty()) {
            throw new NoContentAvailableException("readNotifications", lastId);
        }
        return readNotifications;
    }

    public void updateToRead(long userId, long notificationId) {
        notificationRepository.updateToRead(userId, notificationId);
    }

    public List<NotificationBanner> findNotificationBanners(long userId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();
        return notificationBannerRepository.findUndismissedBanners(
                userId,
                startOfDay,
                now
        );
    }

    public void updateToClosed(long userId, long notificationBannerId) {
        notificationBannerRepository.updateToClosed(userId, notificationBannerId);
    }

    public void deleteNotification(long userId) {
        notificationRepository.deleteNotification(userId);
    }
}
