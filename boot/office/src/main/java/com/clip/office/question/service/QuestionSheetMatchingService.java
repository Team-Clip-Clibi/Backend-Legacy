package com.clip.office.question.service;

import com.clip.matching.entity.OneThingMatching;
import com.clip.matching.entity.RandomMatching;
import com.clip.matching.service.OnethingMatchingService;
import com.clip.matching.service.RandomMatchingService;
import com.clip.office.question.controller.dto.AssignQuestionSheetRequest;
import com.clip.question.entity.Question;
import com.clip.question.entity.QuestionSheet;
import com.clip.question.service.QuestionSheetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionSheetMatchingService {
    private final QuestionSheetService questionSheetService;
    private final OnethingMatchingService onethingMatchingService;
    private final RandomMatchingService randomMatchingService;

    public void assignToOnethingMatchings(AssignQuestionSheetRequest request) {
        validateQuestionCntUnder10(request);

        List<Question> questions = request.questions().stream()
                .map(str -> Question.builder().content(str).build())
                .toList();

        QuestionSheet questionSheet = questionSheetService.saveQuestionSheet(
                QuestionSheet.builder()
                        .questions(questions)
                        .build()
        );

        List<OneThingMatching> oneThingMatchings = onethingMatchingService.findByIds(request.targetMatchingIds()).stream()
                .map(oneThingMatching -> oneThingMatching.updateQuestionSheet(questionSheet))
                .toList();

        onethingMatchingService.saveAll(oneThingMatchings);
    }

    public void assignToRandomMatchings(AssignQuestionSheetRequest request) {
        validateQuestionCntUnder10(request);

        List<Question> questions = request.questions().stream()
                .map(str -> Question.builder().content(str).build())
                .toList();

        QuestionSheet questionSheet = questionSheetService.saveQuestionSheet(
                QuestionSheet.builder()
                        .questions(questions)
                        .build()
        );

        List<RandomMatching> randomMatchings = randomMatchingService.findByIds(request.targetMatchingIds()).stream()
                .map(randomMatching -> randomMatching.updateQuestionSheet(questionSheet))
                .toList();

        randomMatchingService.saveAll(randomMatchings);
    }

    private static void validateQuestionCntUnder10(AssignQuestionSheetRequest request) {
        if (request.questions().size() > 10) {
            throw new IllegalArgumentException("질문지는 최대 10개까지 등록할 수 있습니다.");
        }
    }
}
