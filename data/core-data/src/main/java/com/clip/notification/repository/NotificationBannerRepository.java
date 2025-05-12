package com.clip.notification.repository;

import com.clip.notification.entity.NotificationBanner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationBannerRepository extends JpaRepository<NotificationBanner, Long> {

    @Query("""
    select nb from NotificationBanner nb
    where nb.user.id = :userId
        and nb.isClosed = false
        and nb.createdAt >= :startOfDay
        and nb.createdAt <= :notificationDate
    """)
    List<NotificationBanner> findUndismissedBanners(@Param("userId") Long userId, @Param("startOfDay") LocalDateTime startOfDay, @Param("notificationDate") LocalDateTime notificationDate);

    @Transactional
    @Modifying
    @Query("update NotificationBanner nb set nb.isClosed = true where nb.user.id = :userId and nb.id = :notificationBannerId")
    void updateToClosed(@Param("userId") Long userId, @Param("notificationBannerId") Long notificationBannerId);

}
