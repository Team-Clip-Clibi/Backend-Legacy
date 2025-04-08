package com.clip.notification.repository;

import com.clip.notification.entity.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("""
        select n from Notification n 
        where n.user.id = :userId 
            and n.isRead = false 
            and n.createdAt >= :twoWeeksAgo
            and (:lastId is null or n.id < :lastId)
        order by n.id desc 
    """)
    List<Notification> findUnreadNotifications(
            @Param("userId") long userId,
            @Param("lastId") Long lastId,
            @Param("twoWeeksAgo") LocalDateTime twoWeeksAgo,
            Pageable page
    );

    @Query("""
        select n from Notification n 
        where n.user.id = :userId 
            and (
                n.isRead = true
                or n.createdAt < :twoWeeksAgo
            )
            and (:lastId is null or n.id < :lastId)
        order by n.id desc 
    """)
    List<Notification> findReadNotifications(
            @Param("userId") long userId,
            @Param("lastId") Long lastId,
            @Param("twoWeeksAgo") LocalDateTime twoWeeksAgo,
            Pageable page
    );

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Notification n set n.isRead = true where n.user.id = :userId and n.id = :notificationId")
    void updateToRead(@Param("userId") long userId, @Param("notificationId") long notificationId);
}
