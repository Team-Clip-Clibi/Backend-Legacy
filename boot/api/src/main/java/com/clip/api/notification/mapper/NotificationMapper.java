package com.clip.api.notification.mapper;

import com.clip.api.notification.controller.dto.NotificationBannerDto;
import com.clip.api.notification.controller.dto.NotificationDto;
import com.clip.notification.entity.Notification;
import com.clip.notification.entity.NotificationBanner;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NotificationMapper {
    List<NotificationDto> toNotificationDto(List<Notification> notifications);

    List<NotificationBannerDto> toNotificationBannerDto(List<NotificationBanner> notificationBanners);
}
