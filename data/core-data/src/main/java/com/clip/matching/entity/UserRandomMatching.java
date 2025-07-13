package com.clip.matching.entity;

import com.clip.common.entity.BaseEntity;
import com.clip.order.entity.RandomOrder;
import com.clip.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRandomMatching extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "random_matching_id")
    private RandomMatching randomMatching;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "random_order_id")
    private RandomOrder randomOrder;

    @Column
    private String onethingTopic;

    @Column
    private String tmi;

    @Column
    private boolean isEnded;

    @Enumerated(EnumType.STRING)
    @Column
    private RandomMatchingStatus matchingStatus;

    @Column
    private boolean isNoticeRead;

    @Column
    private Integer lateMinutes;

    @Column
    private boolean isReviewPopupDismissed;

    @Builder
    public UserRandomMatching(User user, RandomMatching randomMatching, RandomOrder randomOrder, String onethingTopic, String tmi, boolean isEnded,
                              RandomMatchingStatus matchingStatus, boolean isNoticeRead, Integer lateMinutes, boolean isReviewPopupDismissed) {
        this.user = user;
        this.randomMatching = randomMatching;
        this.randomOrder = randomOrder;
        this.onethingTopic = onethingTopic;
        this.tmi = tmi;
        this.isEnded = isEnded;
        this.matchingStatus = matchingStatus;
        this.isNoticeRead = isNoticeRead;
        this.lateMinutes = lateMinutes;
        this.isReviewPopupDismissed = isReviewPopupDismissed;
    }

    public void updateStatus(RandomMatchingStatus matchingStatus) {
        this.matchingStatus = matchingStatus;
    }

    public void updateLateMinutes(int lateMinutes) {
        this.lateMinutes = lateMinutes;
    }
}