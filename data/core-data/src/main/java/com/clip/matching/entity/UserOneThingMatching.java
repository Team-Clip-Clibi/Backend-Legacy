package com.clip.matching.entity;

import com.clip.common.entity.BaseEntity;
import com.clip.order.entity.OneThingOrder;
import com.clip.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserOneThingMatching extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "onething_matching_id")
    private OneThingMatching oneThingMatching;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "onething_order_id")
    private OneThingOrder oneThingOrder;

    @Enumerated
    @Column
    private OneThingCategory oneThingCategory;

    @Column
    private String onethingTopic;

    @Column
    private String tmi;

    @Column
    private boolean isEnded;

    @Enumerated(EnumType.STRING)
    @Column
    private OneThingBudgetRange oneThingBudgetRange;

    @Enumerated(EnumType.STRING)
    @Column
    private OnethingDistrict onethingDistrict;

    @Enumerated(EnumType.STRING)
    @Column
    private OnethingKeyword oneThingKeyword;

    @ElementCollection(targetClass = PreferredDate.class)
    @CollectionTable(joinColumns = @JoinColumn(name = "id"))
    @Column
    private List<PreferredDate> preferredDates = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column
    private OneThingMatchingStatus matchingStatus;

    //추후 제거
    @Column
    private boolean isNoticeRead;

    @Column
    private Integer lateMinutes;

    @Column
    private boolean isReviewPopupDismissed;

    @Builder
    public UserOneThingMatching(User user, OneThingMatching oneThingMatching, OneThingOrder oneThingOrder, OneThingCategory oneThingCategory, String onethingTopic, String tmi, boolean isEnded, OnethingDistrict onethingDistrict,
                                List<PreferredDate> preferredDates, OneThingBudgetRange oneThingBudgetRange,
                                OneThingMatchingStatus matchingStatus, boolean isNoticeRead, Integer lateMinutes,
                                OnethingKeyword onethingKeyword, boolean isReviewPopupDismissed) {
        this.user = user;
        this.oneThingMatching = oneThingMatching;
        this.oneThingOrder = oneThingOrder;
        this.oneThingCategory = oneThingCategory;
        this.onethingTopic = onethingTopic;
        this.tmi = tmi;
        this.isEnded = isEnded;
        this.onethingDistrict = onethingDistrict;
        this.preferredDates = preferredDates;
        this.oneThingBudgetRange = oneThingBudgetRange;
        this.matchingStatus = matchingStatus;
        this.isNoticeRead = isNoticeRead;
        this.lateMinutes = lateMinutes;
        this.oneThingKeyword = onethingKeyword;
        this.isReviewPopupDismissed = isReviewPopupDismissed;
    }

    @Embeddable
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PreferredDate implements Comparable<PreferredDate> {
        private LocalDate date;
        @Enumerated(EnumType.STRING)
        private OneThingTimeSlot timeSlot;

        @Builder
        public PreferredDate(LocalDate date, OneThingTimeSlot timeSlot) {
            this.date = date;
            this.timeSlot = timeSlot;
        }

        @Override
        public int compareTo(PreferredDate o) {
            return this.date.compareTo(o.date);
        }
    }

    public void updateLateMinutes(int lastMinutes) {
        this.lateMinutes = lateMinutes;
    }

    public void deleteOnethingMatching() {
        if (Objects.isNull(this.oneThingMatching)) {
            throw new IllegalStateException("매칭이 등록되어 있지 않습니다.");
        }
        this.oneThingMatching = null;
    }

    public void updateOneThingMatching(OneThingMatching oneThingMatching) {
        if (!Objects.isNull(this.oneThingMatching)) {
            throw new IllegalStateException("이미 매칭이 등록되어 있습니다.");
        }
        this.oneThingMatching = oneThingMatching;
        this.matchingStatus = OneThingMatchingStatus.CONFIRMED;
    }

    public UserOneThingMatching updateMatchingStatus(OneThingMatchingStatus matchingStatus) {
        this.matchingStatus = matchingStatus;
        return this;
    }
}
