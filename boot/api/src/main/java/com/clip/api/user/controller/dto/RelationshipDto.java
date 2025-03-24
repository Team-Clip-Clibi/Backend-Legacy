package com.clip.api.user.controller.dto;

import com.clip.user.entity.RelationshipStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class RelationshipDto {
    private RelationshipStatus relationshipStatus;
    @JsonProperty(value = "isSameRelationshipConsidered")
    private Boolean isSameRelationshipConsidered;

    @Builder
    public RelationshipDto(RelationshipStatus relationshipStatus, Boolean isSameRelationshipConsidered) {
        this.relationshipStatus = relationshipStatus;
        this.isSameRelationshipConsidered = isSameRelationshipConsidered;
    }
}
