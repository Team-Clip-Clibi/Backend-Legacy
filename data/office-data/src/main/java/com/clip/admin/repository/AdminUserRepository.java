package com.clip.admin.repository;

import com.clip.admin.entity.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {

    @Query("""
        select au from AdminUser au where au.username = :username
    """)
    Optional<AdminUser> findByUsername(@Param("username") String username);

}