package com.clip.batch.matching.service;


import com.clip.batch.matching.repository.NotificationBannerBatchRepository;
import com.clip.batch.matching.repository.NotificationBatchRepository;
import com.clip.notification.entity.Notification;
import com.clip.notification.entity.NotificationBanner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SaveNotificationService {
    private final NotificationBatchRepository notificationBatchRepository;
    private final NotificationBannerBatchRepository notificationBannerBatchRepository;

    public List<Notification> saveNotifications(List<Notification> notifications) {
        return notificationBatchRepository.saveAll(notifications);
    }

    public List<NotificationBanner> saveNotificationBanners(List<NotificationBanner> notificationBanners) {
        return notificationBannerBatchRepository.saveAll(notificationBanners);
    }

}
