package com.clip.api.user.controller.dto;

import com.clip.user.entity.RelationshipStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UpdateRelationshipDto {
    private RelationshipStatus relationshipStatus;
    @JsonProperty(value = "isSameRelationshipConsidered")
    private boolean isSameRelationshipConsidered;
}
