package com.clip.user.entity;

import com.clip.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30)
    private String username;

    @Column(unique = true)
    private String phoneNumber;

    @Column(unique = true)
    private String nickname;

    @Column
    private LocalDate birth;

    @Enumerated(EnumType.STRING)
    @Column
    private City city;

    @Enumerated(EnumType.STRING)
    @Column
    private County county;

    @Enumerated(EnumType.STRING)
    @Column
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column
    private Platform platform;

    @Column
    private String socialId;

    @Enumerated(EnumType.STRING)
    @Column
    private DeviceType deviceType;

    @Column
    private String firebaseToken;

    @Column
    private String osVersion;

    @Column
    private boolean isVerified;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dietary_option_id")
    private DietaryOption dietaryOption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id")
    private Language language;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "relationship_status_id")
    private RelationshipStatus relationshipStatus;

    @OneToMany(mappedBy = "user")
    private List<UserJob> userJobList;

    @Column
    private boolean isAllowNotify;

    @Builder
    public User(String username, String phoneNumber, String nickname, LocalDate birth, City city, County county, Gender gender, Platform platform, String socialId, DeviceType deviceType, String firebaseToken, String osVersion, boolean isAllowNotify) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.nickname = nickname;
        this.birth = birth;
        this.city = city;
        this.county = county;
        this.gender = gender;
        this.platform = platform;
        this.socialId = socialId;
        this.deviceType = deviceType;
        this.firebaseToken = firebaseToken;
        this.osVersion = osVersion;
        this.isVerified = isAllowNotify;
    }
}
