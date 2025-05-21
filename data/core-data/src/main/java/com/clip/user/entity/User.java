package com.clip.user.entity;

import com.clip.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
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
    private boolean isPhoneNumVerified;

    @Column
    private String language;

    @Column
    private String dietaryOption;

    @Enumerated(EnumType.STRING)
    @Column
    private RelationshipStatus relationshipStatus;

    @Column
    private Boolean isSameRelationshipConsidered;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private Job job;

    @Column
    private boolean isAllowNotify;

    @Builder
    public User(String username, String phoneNumber, String nickname, LocalDate birth, City city, County county, Gender gender, Platform platform, String socialId, DeviceType deviceType, String firebaseToken, String osVersion, boolean isPhoneNumVerified, String language,String dietaryOption, RelationshipStatus relationshipStatus, Boolean isSameRelationshipConsidered, Job job, boolean isAllowNotify) {
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
        this.isPhoneNumVerified = isPhoneNumVerified;
        this.language = language;
        this.dietaryOption = dietaryOption;
        this.relationshipStatus = relationshipStatus;
        this.isSameRelationshipConsidered = isSameRelationshipConsidered;
        this.job = job;
        this.isAllowNotify = isAllowNotify;
    }

    public void updateFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    public void updateIsAllowNotify(boolean isAllowNotify) {
        this.isAllowNotify = isAllowNotify;
    }

    public void updateJob(Job job) {
        this.job = job;
    }

    public void updateRelationshipAndConsidered(RelationshipStatus relationshipStatus, boolean isSameRelationshipConsidered) {
        this.relationshipStatus = relationshipStatus;
        this.isSameRelationshipConsidered = isSameRelationshipConsidered;
    }

    public void updateDietaryOption(String dietaryOption) {
        this.dietaryOption = dietaryOption;
    }

    public void updateLanguage(String language) {
        this.language = language;
    }
}
