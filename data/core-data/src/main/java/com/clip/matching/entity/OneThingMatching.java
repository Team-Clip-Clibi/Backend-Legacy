package com.clip.matching.entity;

import java.time.LocalDateTime;

import com.clip.common.entity.BaseEntity;

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

@Getter
@Entity
@NoArgsConstructor
public class OneThingMatching extends BaseEntity {

    /***
     * 원띵 주문서 엔티티 연관관계 및 홈화면 조회를 위해 미리 생성한 클래스입니다.
     * 추후 구현 시 기본 생성자 접근 제어자를 protected로 변경 후 필요한 컬럼 추가하시고 해당 주석 삭제해주세요.
     */

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
}
