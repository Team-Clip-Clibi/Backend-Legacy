package com.clip.matching.entity;

import java.time.LocalDateTime;

import com.clip.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OneThingMatching extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String address;

    @Column
    private String restaurantName;

    @Column
    private LocalDateTime dateTime;

    @Column
    @Enumerated(EnumType.STRING)
    private OnethingDistrict onethingDistrict;


    @Builder
    public OneThingMatching(String address, String restaurantName, LocalDateTime dateTime, OnethingDistrict onethingDistrict) {
        this.address = address;
        this.restaurantName = restaurantName;
        this.dateTime = dateTime;
        this.onethingDistrict = onethingDistrict;
    }

    public void update(OnethingDistrict oneThingDistrict, String location, String restaurantName, LocalDateTime meetingTime) {
        this.onethingDistrict = oneThingDistrict;
        this.address = location;
        this.restaurantName = restaurantName;
        this.dateTime = meetingTime;
    }
}
