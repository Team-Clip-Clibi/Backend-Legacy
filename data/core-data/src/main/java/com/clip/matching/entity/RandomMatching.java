package com.clip.matching.entity;

import com.clip.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class RandomMatching extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column
    private RandomDistrict randomDistrict;

    @Column
    private String address;

    @Column
    private String restaurantName;

    @Column
    private LocalDateTime dateTime;

    @Column
    private Integer totalCapacity;

    @Builder
    public RandomMatching(RandomDistrict randomDistrict , String address, String restaurantName, LocalDateTime dateTime, Integer totalCapacity) {
        this.randomDistrict = randomDistrict;
        this.address = address;
        this.restaurantName = restaurantName;
        this.dateTime = dateTime;
        this.totalCapacity = totalCapacity;
    }

    public void update(RandomDistrict randomDistrict, String address, String restaurantName, LocalDateTime dateTime, Integer totalCapacity) {
        this.randomDistrict = randomDistrict;
        this.address = address;
        this.restaurantName = restaurantName;
        this.dateTime = dateTime;
        this.totalCapacity = totalCapacity;
    }
}
