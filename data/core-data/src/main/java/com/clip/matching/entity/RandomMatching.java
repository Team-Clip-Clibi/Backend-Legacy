package com.clip.matching.entity;

import com.clip.user.entity.City;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class RandomMatching {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column
    private City city;

    @Column
    private String location;

    @Column
    private String restaurantName;

    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime meetingTime;

    @Builder
    public RandomMatching(Long id, City city, String location, String restaurantName, LocalDateTime meetingTime) {
        this.id = id;
        this.city = city;
        this.location = location;
        this.restaurantName = restaurantName;
        this.meetingTime = meetingTime;
    }

}
