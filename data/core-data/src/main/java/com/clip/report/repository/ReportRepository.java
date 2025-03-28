package com.clip.report.repository;

import com.clip.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    @Query("select r from Report r where r.user.id = :userId")
    List<Report> findReports(@Param("userId") long userId);
}
