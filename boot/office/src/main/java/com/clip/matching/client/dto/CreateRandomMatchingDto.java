package com.clip.matching.client.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CreateRandomMatchingDto {

    private String city;
    private String location;
    private String restaurantName;
    private LocalDateTime meetingTime;

    @Builder
    public CreateRandomMatchingDto(String city, String location, String restaurantName, LocalDateTime meetingTime) {
        this.city = city;
        this.location = location;
        this.restaurantName = restaurantName;
        this.meetingTime = meetingTime;
    }
}