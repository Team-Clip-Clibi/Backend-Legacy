package com.clip.auth.repository;

import com.clip.admin.entity.AdminUser;
import com.clip.auth.entity.AdminToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface AdminTokenRepository extends JpaRepository<AdminToken, Long> {

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update AdminToken t set t.refreshToken = :refreshToken where t.adminUser = :adminUser")
    void updateAdminRefreshToken(@Param("adminUser")AdminUser adminUser,@Param("refreshToken") String refreshToken);

    @Query("select t from AdminToken t where t.adminUser.id = :adminUserId")
    Optional<AdminToken> findAdminToken(@Param("adminUserId") Long adminUserId);
}
