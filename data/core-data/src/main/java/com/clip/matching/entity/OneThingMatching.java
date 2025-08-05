package com.clip.matching.entity;

import java.time.LocalDateTime;

import com.clip.common.entity.BaseEntity;
import com.clip.question.entity.QuestionSheet;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private QuestionSheet questionSheet;

    @Builder
    public OneThingMatching(String address, String restaurantName, LocalDateTime dateTime, OnethingDistrict onethingDistrict) {
        this.address = address;
        this.restaurantName = restaurantName;
        this.dateTime = dateTime;
        this.onethingDistrict = onethingDistrict;
    }

    public void update(OnethingDistrict oneThingDistrict, String address, String restaurantName, LocalDateTime dateTime) {
        this.onethingDistrict = oneThingDistrict;
        this.address = address;
        this.restaurantName = restaurantName;
        this.dateTime = dateTime;
    }

    public OneThingMatching updateQuestionSheet(QuestionSheet questionSheet) {
        this.questionSheet = questionSheet;
        return this;
    }
}
