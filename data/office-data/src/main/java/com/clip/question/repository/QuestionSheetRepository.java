package com.clip.question.repository;

import com.clip.question.entity.QuestionSheet;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuestionSheetRepository extends JpaRepository<QuestionSheet, Long> {
    @Query("SELECT q FROM QuestionSheet q ORDER BY q.id desc ")
    Slice<QuestionSheet> findQuestionSheets(PageRequest page);
}
