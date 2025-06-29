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

    // 변수명 변경
    @Column
    private String myOneThingContent;

    // 변수명 변경
    @Column
    private String myQuizContent;

    // 변수명 변경
    @Column
    private boolean isCheckedMatchingStart;

    @Enumerated(EnumType.STRING)
    @Column
    private OneThingBudgetRange oneThingBudgetRange;

    @Enumerated(EnumType.STRING)
    @Column
    private OneThingDistrict oneThingDistrict;

    @ElementCollection(targetClass = PreferredDate.class)
    @CollectionTable(joinColumns = @JoinColumn(name = "id"))
    @Column
    private List<PreferredDate> preferredDates = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column
    private OneThingMatchingStatus matchingStatus;

    @Column
    private boolean isNoticeRead;

    @Column
    private Integer lateMinutes;

    @Column
    private boolean isReviewPopupDismissed;

    @Builder
    public UserOneThingMatching(User user, OneThingMatching oneThingMatching, OneThingOrder oneThingOrder, OneThingCategory oneThingCategory, String myOneThingContent, String myQuizContent, boolean isCheckedMatchingStart, OneThingDistrict oneThingDistrict,
                                List<PreferredDate> preferredDates, OneThingBudgetRange oneThingBudgetRange,
                                OneThingMatchingStatus matchingStatus, boolean isNoticeRead, Integer lateMinutes, boolean isReviewPopupDismissed) {
        this.user = user;
        this.oneThingMatching = oneThingMatching;
        this.oneThingOrder = oneThingOrder;
        this.oneThingCategory = oneThingCategory;
        this.myOneThingContent = myOneThingContent;
        this.myQuizContent = myQuizContent;
        this.isCheckedMatchingStart = isCheckedMatchingStart;
        this.oneThingDistrict = oneThingDistrict;
        this.preferredDates = preferredDates;
        this.oneThingBudgetRange = oneThingBudgetRange;
        this.matchingStatus = matchingStatus;
        this.isNoticeRead = isNoticeRead;
        this.lateMinutes = lateMinutes;
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
}
