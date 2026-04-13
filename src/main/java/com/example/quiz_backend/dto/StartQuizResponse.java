package com.example.quiz_backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StartQuizResponse {
    private String quizSessionId;
    private Long quizId;
    private QuizProgress progress;
    private QuizQuestion question;
}
