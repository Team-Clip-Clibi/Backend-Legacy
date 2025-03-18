package com.clip.user.repository;

import com.clip.user.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    @Query("""
        select u from User u
        where u.socialId = :socialId and u.platform = :platform
    """)
    Optional<User> findUser(@Param("socialId") String socialId, @Param("platform") Platform platform);

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update User u set u.phoneNumber = :phoneNumber, u.isVerified = true where u.id = :userId")
    void updatePhoneNumber(@Param("userId") long userId, @Param("phoneNumber") String phoneNumber);

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update User u set u.username = :userName where u.id = :userId")
    void updateUserName(@Param("userId") long userId, @Param("userName") String userName);

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update User u set u.nickname = :nickname where u.id = :userId")
    void updateNickname(@Param("userId") long userId, @Param("nickname") String nickname);

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update User u set u.deviceType = :deviceType, u.osVersion = :osVersion, u.firebaseToken = :fireBaseToken where u.id = :userId")
    void updateDeviceInfo(
            @Param("userId") Long userId,
            @Param("deviceType") DeviceType deviceType,
            @Param("osVersion") String osVersion,
            @Param("fireBaseToken") String firebaseToken
    );

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
                update User u set u.gender = :gender, u.birth = :birth, u.city = :city, u.county = :county
                where u.id = :userId
            """)
    void updateUserDetailInfo(
            @Param("userId") long userId,
            @Param("gender") Gender gender,
            @Param("birth") LocalDate birth,
            @Param("city") City city,
            @Param("county") County county
    );

    @Query("select u from User u where u.phoneNumber = :phoneNumber")
    Optional<User> findUser(@Param("phoneNumber") String phoneNumber);

    boolean existsByNickname(String nickname);
}
