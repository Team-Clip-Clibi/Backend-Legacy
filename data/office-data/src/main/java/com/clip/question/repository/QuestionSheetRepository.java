package com.clip.question.repository;

import com.clip.question.entity.QuestionSheet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionSheetRepository extends JpaRepository<QuestionSheet, Long> {
}
