package com.clip.auth.repository;

import com.clip.auth.entity.Token;
import com.clip.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Token t set t.refreshToken = :refreshToken, t.updatedAt = :updatedAt where t.user = :user")
    void updateRefreshToken(@Param("user") User user, @Param("refreshToken") String refreshToken, @Param("updatedAt") LocalDateTime updatedAt);

    @Query("select t from Token t where t.user.id = :userId")
    Optional<Token> findToken(@Param("userId") Long userId);

    @Query("select t.user from Token t where t.refreshToken = :refreshToken")
    Optional<User> findUser(@Param("refreshToken") String refreshToken);
}
