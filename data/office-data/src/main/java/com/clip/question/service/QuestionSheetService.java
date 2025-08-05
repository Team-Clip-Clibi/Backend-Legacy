package com.clip.question.service;

import com.clip.question.entity.QuestionSheet;
import com.clip.question.repository.QuestionSheetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionSheetService {
    private final QuestionSheetRepository questionSheetRepository;

    @Transactional
    public QuestionSheet saveQuestionSheet(QuestionSheet questionSheet) {
        return questionSheetRepository.save(questionSheet);
    }

}
