package com.clip.api.user.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LanguageDto {
    private Language language;

    @Builder
    public LanguageDto(Language language) {
        this.language = language;
    }
}
