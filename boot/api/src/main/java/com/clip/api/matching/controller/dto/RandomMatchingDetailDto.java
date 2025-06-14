package com.clip.api.matching.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder
@Schema(name = "RandomMatchingDetailDto", description = "랜덤 매칭 상세 정보", allOf = {MatchingDetailDto.class})
public class RandomMatchingDetailDto extends MatchingDetailDto {

    @Getter
    @SuperBuilder
    @Schema(name = "RandomMatchingApplicationInfo")
    public static class ApplicationInfo extends MatchingDetailDto.ApplicationInfo {

    }
}
