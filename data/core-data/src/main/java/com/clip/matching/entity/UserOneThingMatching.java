package com.clip.matching.entity;

import com.clip.common.entity.BaseEntity;
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

    @ElementCollection(targetClass = PreferredDate.class)
    @CollectionTable(joinColumns = @JoinColumn(name = "id"))
    @Column
    private List<PreferredDate> preferredDates = new ArrayList<>();

    @Column
    private MatchingStatus matchingStatus;

    @Column
    private boolean isNoticeRead;

    @Builder
    public UserOneThingMatching(User user, OneThingMatching oneThingMatching, String myOneThingContent, String myQuizContent, boolean isCheckedMatchingStart,
                                List<PreferredDate> preferredDates, OneThingBudgetRange oneThingBudgetRange,
        MatchingStatus matchingStatus, boolean isNoticeRead) {
        this.user = user;
        this.oneThingMatching = oneThingMatching;
        this.myOneThingContent = myOneThingContent;
        this.myQuizContent = myQuizContent;
        this.isCheckedMatchingStart = isCheckedMatchingStart;
        this.preferredDates = preferredDates;
        this.oneThingBudgetRange = oneThingBudgetRange;
        this.matchingStatus = matchingStatus;
        this.isNoticeRead = isNoticeRead;
    }

    @Embeddable
    @Getter
    public static class PreferredDate{
        private LocalDate date;
        @Enumerated(EnumType.STRING)
        private OneThingTimeSlot timeSlot;
    }
}
