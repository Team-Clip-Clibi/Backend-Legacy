package com.clip.office.question.controller.dto;

import java.util.List;

public record AssignQuestionSheetRequest(
        List<Long> targetMatchingIds,
        List<String> questions
) {
}
