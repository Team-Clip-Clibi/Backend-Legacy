package com.clip.notice.repository;

import com.clip.notice.entity.Notice;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from Notice n where n.id = :noticeId")
    void deleteNotice(@Param("noticeId") Long noticeId);

    @Query("select n from Notice n where n.id = :noticeId")
    Optional<Notice> findNotice(Long noticeId);

    @Query("""
            select n from Notice n
            where n.exposureDate <= :exposureDate
            and n.isExposure = true
            order by n.exposureDate desc
            limit 3
            """)
    List<Notice> findAllNotices(@Param("exposureDate") LocalDate exposureDate);

}
