package com.clip.office.matching.controller.dto;

import com.clip.matching.entity.RandomDistrict;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CreateRandomMatchingDto {

    private RandomDistrict randomDistrict;
    private String location;
    private String restaurantName;
    private LocalDateTime meetingTime;

    @Builder
    public CreateRandomMatchingDto(RandomDistrict randomDistrict,String location, String restaurantName, LocalDateTime meetingTime) {
        this.randomDistrict = randomDistrict;
        this.location = location;
        this.restaurantName = restaurantName;
        this.meetingTime = meetingTime;
    }
}