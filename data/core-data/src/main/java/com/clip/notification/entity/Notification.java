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
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column
    private NotificationType notificationType;

    @Column
    private boolean isRead;

    @Enumerated(EnumType.STRING)
    @Column
    private SendStatus sendStatus;

    private String messageTemplateType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    User user;

    @Column
    private Long matchingId;

    @Column
    private String matchingType;

    @Builder
    public Notification(NotificationType notificationType, boolean isRead, String messageTemplateType, User user, Long matchingId, String matchingType) {
        this.notificationType = notificationType;
        this.isRead = isRead;
        this.messageTemplateType = messageTemplateType;
        this.user = user;
        this.sendStatus = SendStatus.SENT;
        this.matchingId = matchingId;
        this.matchingType = matchingType;
    }
}
