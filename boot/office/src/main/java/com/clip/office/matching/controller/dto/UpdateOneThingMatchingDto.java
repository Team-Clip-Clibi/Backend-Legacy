package com.clip.office.matching.controller.dto;

import com.clip.matching.entity.OneThingDistrict;
import com.clip.matching.entity.OneThingPrice;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UpdateOneThingMatchingDto {
    private OneThingDistrict oneThingDistrict;
    private String location;
    private String restaurantName;
    private LocalDateTime meetingTime;
    private OneThingPrice oneThingPrice;

    @Builder
    public UpdateOneThingMatchingDto(OneThingDistrict oneThingDistrict, String location, String restaurantName, LocalDateTime meetingTime, OneThingPrice oneThingPrice) {
        this.oneThingDistrict = oneThingDistrict;
        this.location = location;
        this.restaurantName = restaurantName;
        this.meetingTime = meetingTime;
        this.oneThingPrice = oneThingPrice;
    }
}
