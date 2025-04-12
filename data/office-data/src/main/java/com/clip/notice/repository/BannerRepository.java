package com.clip.notice.repository;

import com.clip.notice.entity.Banner;
import com.clip.notice.entity.BannerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import java.time.LocalDate;
import java.util.List;

public interface BannerRepository extends JpaRepository<Banner, Long> {

    @Query("select b from Banner b where b.id = :bannerId")
    Optional<Banner> findBanner(@Param("bannerId") Long bannerId);


    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from Banner b where b.id = :bannerId")
    void deleteBanner(@Param("bannerId")Long bannerId);

    @Query(value = """
            select b
            from Banner b
            where b.isExposure = true
            and b.bannerType = :bannerType
            and b.exposureDate <= :exposureDate
            order by b.exposureDate desc
            limit 3
            """)
    List<Banner> findByBannerType(
            @Param("bannerType") BannerType bannerType,
            @Param("exposureDate") LocalDate exposureDate
    );
}
