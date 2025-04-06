package com.clip.api.matching.controller.dto;

import com.clip.matching.entity.Mood;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MatchingReviewDto {
    private Mood mood;
    private String positivePoints;
    private String negativePoints;
    private String reviewContent;
    private Boolean isMemberAllAttended;
    private String noShowMembers;

    @Builder
    public MatchingReviewDto(Mood mood, String positivePoints, String negativePoints,
                             String reviewContent, Boolean isMemberAllAttended, String noShowMembers) {
        this.mood = mood;
        this.positivePoints = positivePoints;
        this.negativePoints = negativePoints;
        this.reviewContent = reviewContent;
        this.isMemberAllAttended = isMemberAllAttended;
        this.noShowMembers = noShowMembers;
    }
}
