package com.clip.office.question.controller;

import com.clip.office.question.controller.dto.AssignQuestionSheetRequest;
import com.clip.office.question.service.QuestionSheetMatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/question-sheets")
@RequiredArgsConstructor
public class QuestionSheetController {
    private final QuestionSheetMatchingService questionSheetMatchingService;

    @PostMapping("/onethings")
    public void assignToOnethingMatchings(@RequestBody AssignQuestionSheetRequest request) {
        questionSheetMatchingService.assignToOnethingMatchings(request);
    }

    @PostMapping("/randoms")
    public void assignToRandomMatchings(@RequestBody AssignQuestionSheetRequest request) {
        questionSheetMatchingService.assignToRandomMatchings(request);
    }
}
