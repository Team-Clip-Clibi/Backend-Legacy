package com.clip.matching.entity;

import com.clip.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"random_matching_id", "user_id"})})
@NoArgsConstructor
public class RandomMatchingReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "random_matching_id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private RandomMatching randomMatching;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Mood mood;

    @Column(nullable = false)
    private String positivePoints;

    @Column(nullable = false)
    private String negativePoints;

    @Column
    private String reviewContent;

    @Column
    private Boolean isMemberAllAttended;

    @Column
    private String noShowMembers;

    @Column
    private Boolean isReviewPopupDismissed;

    @Builder
    public RandomMatchingReview(RandomMatching randomMatching, User user, Mood mood,
                                String positivePoints, String negativePoints, String reviewContent,
                                Boolean isMemberAllAttended, String noShowMembers, Boolean isReviewPopupDismissed) {
        this.randomMatching = randomMatching;
        this.user = user;
        this.mood = mood;
        this.positivePoints = positivePoints;
        this.negativePoints = negativePoints;
        this.reviewContent = reviewContent;
        this.isMemberAllAttended = isMemberAllAttended;
        this.noShowMembers = noShowMembers;
        this.isReviewPopupDismissed = isReviewPopupDismissed;
    }
}
