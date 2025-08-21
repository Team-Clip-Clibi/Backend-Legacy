package com.clip.office.matching.controller.dto;

import com.clip.matching.entity.RandomDistrict;

import java.time.LocalDateTime;

public record CreateRandomMatchingDto(
        RandomDistrict randomDistrict,
        String restaurantName,
        String address,
        String menu,
        String cuisineType,
        LocalDateTime dateTime
) {
}
