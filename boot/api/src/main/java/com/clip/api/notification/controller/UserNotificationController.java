package com.clip.api.notification.controller;

import com.clip.api.docs.notification.UserNotificationDocs;
import com.clip.api.notification.controller.dto.NotificationDto;
import com.clip.api.notification.service.UserNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserNotificationController implements UserNotificationDocs {
    private final UserNotificationService userNotificationService;


    @Override
    public List<NotificationDto> findUnreadNotifications(Long lastId, UserDetails userDetails) {
        return userNotificationService.getUnreadNotifications(Long.parseLong(userDetails.getUsername()), lastId);
    }

    @Override
    public List<NotificationDto> findReadNotifications(Long lastId, UserDetails userDetails) {
        return userNotificationService.getReadNotifications(Long.parseLong(userDetails.getUsername()), lastId);
    }

    @Override
    public void updateToReadStatus(Long lastId, UserDetails userDetails) {
        userNotificationService.updateToRead(Long.parseLong(userDetails.getUsername()), lastId);
    }
}
