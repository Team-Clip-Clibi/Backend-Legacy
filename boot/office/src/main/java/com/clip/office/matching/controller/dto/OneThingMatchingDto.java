package com.clip.office.matching.controller.dto;

import com.clip.matching.entity.OneThingDistrict;
import com.clip.matching.entity.OneThingKeyword;
import com.clip.matching.entity.OneThingPrice;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class OneThingMatchingDto {

    private OneThingDistrict oneThingDistrict;
    private OneThingKeyword oneThingKeyword;
    private String location;
    private String restaurantName;
    private LocalDateTime meetingTime;
    private OneThingPrice oneThingPrice;

    @Builder
    public OneThingMatchingDto(OneThingDistrict oneThingDistrict, OneThingKeyword oneThingKeyword, String location,
        String restaurantName, LocalDateTime meetingTime, OneThingPrice oneThingPrice) {
        this.oneThingDistrict = oneThingDistrict;
        this.oneThingKeyword = oneThingKeyword;
        this.location = location;
        this.restaurantName = restaurantName;
        this.meetingTime = meetingTime;
        this.oneThingPrice = oneThingPrice;
    }
}
