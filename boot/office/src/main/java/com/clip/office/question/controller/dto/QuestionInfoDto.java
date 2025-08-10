package com.clip.office.question.controller.dto;

import java.util.List;

public record QuestionInfoDto(
        long id,
        String title,
        List<String> questions
) {
}
