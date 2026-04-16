package com.example.quiz_backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuizProgress {
    private int currentQuestionNumber;
    private int totalQuestionCount;
    private int answeredCount;
    private int correctCount;
    private int incorrectCount;
}