package com.clip.notification.event;

import com.clip.infra.fcm.event.FcmSendFailedEvent;
import com.clip.notification.entity.SendStatus;
import com.clip.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FcmSendFailedEventListener {
    private final NotificationRepository notificationRepository;

    @EventListener
    @Transactional
    public void handleFcmSendFailedEvent(FcmSendFailedEvent event) {
        List<Long> failedNotificationIds = event.getNotificationIds();

        if (failedNotificationIds == null || failedNotificationIds.isEmpty())
            log.warn("FCM 실패 이벤트 수신됨: 실패한 알림 ID가 없습니다.");

        // 실패한 알림 상태 변경
        notificationRepository.updateFcmSendStatus(SendStatus.FAILED, failedNotificationIds);
    }

}
