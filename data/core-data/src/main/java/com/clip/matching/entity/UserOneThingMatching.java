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

    @Column
    private String myOneThingContent;

    @Column
    private String myQuizContent;

    @Column
    private boolean isCheckedMatchingStart;

    @Builder
    public UserOneThingMatching(User user, OneThingMatching oneThingMatching, String myOneThingContent, String myQuizContent, boolean isCheckedMatchingStart) {
        this.user = user;
        this.oneThingMatching = oneThingMatching;
        this.myOneThingContent = myOneThingContent;
        this.myQuizContent = myQuizContent;
        this.isCheckedMatchingStart = isCheckedMatchingStart;
    }
}
