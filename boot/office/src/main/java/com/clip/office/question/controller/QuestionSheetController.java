package com.clip.office.question.controller;

import com.clip.office.question.controller.dto.AssignQuestionSheetRequest;
import com.clip.office.question.controller.dto.MatchingQuestionInfoDto;
import com.clip.office.question.controller.dto.QuestionInfoDto;
import com.clip.office.question.service.QuestionSheetMatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/question-sheets")
@RequiredArgsConstructor
public class QuestionSheetController {
    private final QuestionSheetMatchingService questionSheetMatchingService;

    /**
     * 질문 내용 수정 API
     */
    @PatchMapping("/{id}")
    public void updateQuestionSheet(@PathVariable long id, @RequestBody QuestionInfoDto request) {
        questionSheetMatchingService.updateQuestionSheet(id, request);
    }

    /**
     * 질문 목록(질문 내용 포함) API
     */
    @GetMapping("/{page}")
    public Slice<QuestionInfoDto> getQuestionSheetsFetchQuestions(@PathVariable int page) {
        return questionSheetMatchingService.getQuestionSheetsFetchQuestions(page);
    }

    /**
     * 질문 생성 여부에 따른 원띵 모임 조회 API
     */
    @GetMapping("/onethings/{page}")
    public Slice<MatchingQuestionInfoDto> getOnethingMatchings(@RequestParam boolean hasQuestion, @PathVariable int page) {
        return questionSheetMatchingService.getOnethingMatchings(hasQuestion, page);
    }

    /**
     * 원띵 모임 질문지 생성 API
     */
    @PostMapping("/onethings")
    public void assignToOnethingMatchings(@RequestBody AssignQuestionSheetRequest request) {
        questionSheetMatchingService.assignToOnethingMatchings(request);
    }

    /**
     * 질문 생성 여부에 따른 랜덤 모임 조회 API
     */
    @GetMapping("/randoms/{page}")
    public Slice<MatchingQuestionInfoDto> getRandomMatchings(@RequestParam boolean hasQuestion, @PathVariable int page) {
        return questionSheetMatchingService.getRandomMatchings(hasQuestion, page);
    }

    /**
     * 랜덤 모임 질문지 생성 API
     */
    @PostMapping("/randoms")
    public void assignToRandomMatchings(@RequestBody AssignQuestionSheetRequest request) {
        questionSheetMatchingService.assignToRandomMatchings(request);
    }
}
