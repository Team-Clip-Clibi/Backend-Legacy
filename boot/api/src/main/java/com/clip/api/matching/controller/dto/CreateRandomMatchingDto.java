package com.clip.api.matching.controller.dto;

import com.clip.user.entity.City;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CreateRandomMatchingDto {

    private City city;
    private String location;
    private String restaurantName;
    private LocalDateTime meetingTime;

    @Builder
    public CreateRandomMatchingDto(City city, String location, String restaurantName, LocalDateTime meetingTime) {
        this.city = city;
        this.location = location;
        this.restaurantName = restaurantName;
        this.meetingTime = meetingTime;
    }
}