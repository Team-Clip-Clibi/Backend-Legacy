package com.clip.banner.repository;

import com.clip.banner.entity.Banner;
import com.clip.banner.entity.BannerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface BannerRepository extends JpaRepository<Banner, Long> {

    @Query(value = """
            select b
            from Banner b
            where b.bannerType = :bannerType
            and b.exposureDatetime <= :exposureDatetime
            order by b.exposureDatetime desc
            limit 3
            """)
    List<Banner> findByBannerType(
            @Param("bannerType") BannerType bannerType,
            @Param("exposureDatetime") LocalDateTime exposureDatetime
    );

    @Query("select b from Banner b where b.bannerType = :bannerType ")
    List<Banner> findByBannerType(@Param("bannerType") BannerType bannerType);
}
