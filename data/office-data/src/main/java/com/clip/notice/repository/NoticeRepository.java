package com.clip.notice.repository;

import com.clip.notice.entity.Notice;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    @Query("""
            select n from Notice n
            where n.exposureDateTime <= :exposureDateTime
            order by n.exposureDateTime desc
            limit 3
            """)
    List<Notice> findExposedNotice(@Param("exposureDateTime") LocalDateTime exposureDateTime);

}
