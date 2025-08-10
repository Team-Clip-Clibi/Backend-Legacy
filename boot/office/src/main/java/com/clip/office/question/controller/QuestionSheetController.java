package com.clip.office.question.controller;

import com.clip.office.question.controller.dto.AssignQuestionSheetRequest;
import com.clip.office.question.controller.dto.MatchingQuestionInfoDto;
import com.clip.office.question.service.QuestionSheetMatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/question-sheets")
@RequiredArgsConstructor
public class QuestionSheetController {
    private final QuestionSheetMatchingService questionSheetMatchingService;

    @GetMapping("/onethings/{page}")
    public Slice<MatchingQuestionInfoDto> getOnethingMatchings(@PathVariable int page) {
        return questionSheetMatchingService.getOnethingMatchingsFetchQuestion(page);
    }

    @PostMapping("/onethings")
    public void assignToOnethingMatchings(@RequestBody AssignQuestionSheetRequest request) {
        questionSheetMatchingService.assignToOnethingMatchings(request);
    }

    @GetMapping("/randoms/{page}")
    public Slice<MatchingQuestionInfoDto> getRandomMatchings(@PathVariable int page) {
        return questionSheetMatchingService.getRandomMatchingsFetchQuestion(page);
    }

    @PostMapping("/randoms")
    public void assignToRandomMatchings(@RequestBody AssignQuestionSheetRequest request) {
        questionSheetMatchingService.assignToRandomMatchings(request);
    }
}
