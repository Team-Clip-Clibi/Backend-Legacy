package com.clip.matching.entity;

import com.clip.common.entity.BaseEntity;
import com.clip.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class UserOneThingMatching extends BaseEntity {

    /***
     * 홈 화면 조회를 위해 미리 생성한 클래스입니다.
     * 추후 구현 시 기본 생성자 접근 제어자를 protected로 변경 후 필요한 컬럼 추가하시고 해당 주석 삭제해주세요.
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "onething_matching_id")
    private OneThingMatching oneThingMatching;

    @Column
    private String MyOneThingContent;

}
