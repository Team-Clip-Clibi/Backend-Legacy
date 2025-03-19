package com.clip.api.user.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateNotifyAllowDto {
    @JsonProperty("isAllowNotify")
    private boolean isAllowNotify;
}
