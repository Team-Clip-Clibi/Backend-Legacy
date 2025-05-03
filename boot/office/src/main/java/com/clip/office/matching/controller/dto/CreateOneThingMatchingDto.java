package com.clip.office.matching.controller.dto;

import com.clip.matching.entity.OneThingDistrict;
import com.clip.matching.entity.OneThingKeyword;
import com.clip.matching.entity.OneThingBudgetRange;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CreateOneThingMatchingDto {

    private OneThingDistrict oneThingDistrict;
    private OneThingKeyword oneThingKeyword;
    private String location;
    private String restaurantName;
    private LocalDateTime meetingTime;
    private OneThingBudgetRange oneThingPrice;

    @Builder
    public CreateOneThingMatchingDto(OneThingDistrict oneThingDistrict, OneThingKeyword oneThingKeyword,
        String location,
        String restaurantName, LocalDateTime meetingTime, OneThingBudgetRange oneThingPrice) {
        this.oneThingDistrict = oneThingDistrict;
        this.oneThingKeyword = oneThingKeyword;
        this.location = location;
        this.restaurantName = restaurantName;
        this.meetingTime = meetingTime;
        this.oneThingPrice = oneThingPrice;
    }
}
