package com.clip.notification.entity;

import com.clip.common.entity.BaseEntity;
import com.clip.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationBanner extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    User user;

    @Enumerated(EnumType.STRING)
    @Column
    private NotificationBannerType notificationBannerType;

    @Column
    private boolean isClosed;

    @Builder
    public NotificationBanner(User user, NotificationBannerType notificationBannerType, boolean isClosed) {
        this.user = user;
        this.notificationBannerType = notificationBannerType;
        this.isClosed = isClosed;
    }
}
