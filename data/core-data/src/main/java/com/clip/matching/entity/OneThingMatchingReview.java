package com.clip.matching.entity;

import com.clip.common.entity.BaseEntity;
import com.clip.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"onething_matching_id", "user_id"})})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OneThingMatchingReview extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "onething_matching_id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private OneThingMatching oneThingMatching;

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
    private boolean isMemberAllAttended;

    @Column
    private String noShowMembers;

    @Builder
    public OneThingMatchingReview(OneThingMatching oneThingMatching, User user, Mood mood,
                                  String positivePoints, String negativePoints, String reviewContent,
                                  boolean isMemberAllAttended, String noShowMembers) {
        this.oneThingMatching = oneThingMatching;
        this.user = user;
        this.mood = mood;
        this.positivePoints = positivePoints;
        this.negativePoints = negativePoints;
        this.reviewContent = reviewContent;
        this.isMemberAllAttended = isMemberAllAttended;
        this.noShowMembers = noShowMembers;
    }
}
