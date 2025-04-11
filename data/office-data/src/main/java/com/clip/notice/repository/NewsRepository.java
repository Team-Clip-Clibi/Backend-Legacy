package com.clip.notice.repository;

import com.clip.notice.entity.News;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface NewsRepository extends JpaRepository<News, Long> {

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from News n where n.id = :newsId")
    void deleteNews(@Param("newsId") Long newsId);

    @Query("select n from News n where n.id = :newsId")
    Optional<News> findNews(Long newsId);
}
