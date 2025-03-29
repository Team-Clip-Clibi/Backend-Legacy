package com.clip.matching.entity;

import com.clip.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class OneThingMatching extends BaseEntity {

    /***
     * 원띵 주문서 엔티티 연관관계를 위해 미리 생성한 클래스입니다.
     * 추후 구현 시 기본 생성자 접근 제어자를 protected로 변경 후 필요한 컬럼 추가하시고 해당 주석 삭제해주세요.
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
