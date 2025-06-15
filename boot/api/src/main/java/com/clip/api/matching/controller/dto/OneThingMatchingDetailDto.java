package com.clip.api.matching.controller.dto;

import com.clip.matching.entity.OneThingBudgetRange;
import com.clip.matching.entity.OneThingCategory;
import com.clip.matching.entity.UserOneThingMatching;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@NoArgsConstructor
@SuperBuilder
@Schema(name = "OneThingMatchingDetailDto", description = "원띵 매칭 상세 정보", allOf = {MatchingDetailDto.class})
public class OneThingMatchingDetailDto extends MatchingDetailDto {

    @Getter
    @Schema(name = "OneThingMatchingApplicationInfo")
    @SuperBuilder
    public static class ApplicationInfo extends MatchingDetailDto.ApplicationInfo {
        private List<UserOneThingMatching.PreferredDate> preferredDates;
        private OneThingBudgetRange oneThingBudgetRange;
        private OneThingCategory oneThingCategory;
    }
}

