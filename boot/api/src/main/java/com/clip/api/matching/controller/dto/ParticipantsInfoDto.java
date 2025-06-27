package com.clip.api.matching.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ParticipantsInfoDto {
    private long id;
    private String nickname;
}
