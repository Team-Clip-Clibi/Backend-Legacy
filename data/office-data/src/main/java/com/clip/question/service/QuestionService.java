package com.clip.question.service;

import com.clip.question.entity.Question;
import com.clip.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    public List<Question> saveAll(List<Question> questionList) {
        return questionRepository.saveAll(questionList);
    }
}
