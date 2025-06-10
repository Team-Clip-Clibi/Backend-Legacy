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
    private String myOneThingContent;

    @Column
    private boolean isCheckedMatchingStart;

    @Enumerated(EnumType.STRING)
    @Column
    private MatchingStatus matchingStatus;

    @Column
    private boolean isNoticeRead;

    @Builder
    public UserRandomMatching(User user, RandomMatching randomMatching, RandomOrder randomOrder, String myOneThingContent, boolean isCheckedMatchingStart,
                              MatchingStatus matchingStatus, boolean isNoticeRead) {
        this.user = user;
        this.randomMatching = randomMatching;
        this.randomOrder = randomOrder;
        this.myOneThingContent = myOneThingContent;
        this.isCheckedMatchingStart = isCheckedMatchingStart;
        this.matchingStatus = matchingStatus;
        this.isNoticeRead = isNoticeRead;
    }

    public void updateStatus(MatchingStatus matchingStatus) {
        this.matchingStatus = matchingStatus;
    }
}