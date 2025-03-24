package com.clip.api.user.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DietaryDto {
    private String dietaryOption;

    @Builder
    public DietaryDto(String dietaryOption) {
        this.dietaryOption = dietaryOption;
    }
}
