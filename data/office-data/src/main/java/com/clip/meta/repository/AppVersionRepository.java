package com.clip.meta.repository;

import com.clip.meta.entity.AppVersion;
import com.clip.meta.entity.OSType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AppVersionRepository extends JpaRepository<AppVersion, Long> {
    @Query("select a.version " +
            "from AppVersion a " +
            "where a.type = :type " +
            "order by a.id limit 1")
    String findMinVersion(OSType type);
}
