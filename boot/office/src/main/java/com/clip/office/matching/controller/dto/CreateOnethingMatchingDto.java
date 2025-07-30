package com.clip.office.matching.controller.dto;

import com.clip.matching.entity.OnethingDistrict;

import java.time.LocalDateTime;

public record CreateOnethingMatchingDto(
        OnethingDistrict onethingDistrict,
        String restaurantName,
        String address,
        LocalDateTime dateTime
) {
}
