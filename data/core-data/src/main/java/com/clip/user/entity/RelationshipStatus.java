package com.clip.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RelationshipStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String status;

    @Column
    private boolean isAgreed; //연애 상태가 같은 상태인지 아닌지

    @OneToMany(mappedBy = "relationshipStatus")
    private List<User> userlist;
}
