package com.clip.community.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
public class Community {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String topic;

    @Column
    private String question;

    @OneToMany(mappedBy = "community")
    private List<CommunityLocation> communityLocationList;

    @OneToMany(mappedBy = "community")
    private List<UserCommunity> usercommunityList;
}
