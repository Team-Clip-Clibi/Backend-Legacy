package com.clip.community.entity;

import com.clip.user.entity.City;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
public class RandomMatching {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime meetingTime;

    @Column
    private City city;

    @Column
    private String location;

    @Builder
    public RandomMatching(Long id, LocalDateTime meetingTime) {
        this.id = id;
        this.meetingTime = meetingTime;
    }
}
