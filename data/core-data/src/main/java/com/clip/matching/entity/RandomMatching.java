package com.clip.matching.entity;

import com.clip.common.entity.BaseEntity;
import com.clip.question.entity.QuestionSheet;
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
    private String cuisineType;

    @Column
    private String menu;

    @Column
    private LocalDateTime dateTime;

    @Column
    private Integer totalCapacity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private QuestionSheet questionSheet;

    @Builder
    public RandomMatching(RandomDistrict randomDistrict , String address, String restaurantName, String menu, String cuisineType, LocalDateTime dateTime, Integer totalCapacity) {
        this.randomDistrict = randomDistrict;
        this.address = address;
        this.restaurantName = restaurantName;
        this.menu = menu;
        this.cuisineType = cuisineType;
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

    public RandomMatching updateQuestionSheet(QuestionSheet questionSheet) {
        this.questionSheet = questionSheet;
        return this;
    }
}
