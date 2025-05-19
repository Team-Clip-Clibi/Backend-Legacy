package com.clip.matching.entity;

import com.clip.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

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
    private String location;

    @Column
    private String restaurantName;

    @Column
    private LocalDateTime meetingTime;

    @Builder
    public RandomMatching(RandomDistrict randomDistrict ,String location, String restaurantName, LocalDateTime meetingTime) {
        this.randomDistrict = randomDistrict;
        this.location = location;
        this.restaurantName = restaurantName;
        this.meetingTime = meetingTime;
    }

    public void update(RandomDistrict randomDistrict, String location, String restaurantName, LocalDateTime meetingTime) {
        this.randomDistrict = randomDistrict;
        this.location = location;
        this.restaurantName = restaurantName;
        this.meetingTime = meetingTime;
    }
}
