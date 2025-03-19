package com.clip.match.random.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
public class RandomMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String topic;

    @OneToMany(mappedBy = "randomMatch")
    private List<RandomMatchLocation> randomMatchLocationList;

    @OneToMany(mappedBy = "randomMatch")
    private List<UserRandomMatch> userRandomMatchList;
}
