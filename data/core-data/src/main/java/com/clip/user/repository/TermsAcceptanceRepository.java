package com.clip.user.repository;

import com.clip.user.entity.TermsAcceptance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface TermsAcceptanceRepository extends JpaRepository<TermsAcceptance, Long> {
    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from TermsAcceptance t where t.user.id = :userId")
    void deleteTermsAcceptance(@Param("userId") long userId);
}
