package com.clip.matching.entity;

import java.time.LocalDateTime;

import com.clip.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OneThingMatching extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private OneThingDistrict oneThingDistrict;

    @Column
    @Enumerated(EnumType.STRING)
    private OneThingKeyword oneThingKeyword;

    @Column
    private String location;

    @Column
    private String restaurantName;

    @Column
    private LocalDateTime meetingTime;

    @Column
    @Enumerated(EnumType.STRING)
    private OneThingPrice oneThingPrice;

    @Builder
    public OneThingMatching(OneThingDistrict oneThingDistrict, OneThingKeyword oneThingKeyword, String location, String restaurantName, LocalDateTime meetingTime, OneThingPrice oneThingPrice) {
        this.oneThingDistrict = oneThingDistrict;
        this.oneThingKeyword = oneThingKeyword;
        this.location = location;
        this.restaurantName = restaurantName;
        this.meetingTime = meetingTime;
        this.oneThingPrice = oneThingPrice;
    }

    public void update(OneThingDistrict oneThingDistrict, String location, String restaurantName, LocalDateTime meetingTime, OneThingPrice oneThingPrice) {
        this.oneThingDistrict = oneThingDistrict;
        this.location = location;
        this.restaurantName = restaurantName;
        this.meetingTime = meetingTime;
        this.oneThingPrice = oneThingPrice;
    }

    @Builder
    public OneThingMatching(String location, LocalDateTime meetingTime) {
        this.location = location;
        this.meetingTime = meetingTime;
    }
}
