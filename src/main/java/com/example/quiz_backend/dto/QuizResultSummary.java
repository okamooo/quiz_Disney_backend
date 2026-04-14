package com.example.quiz_backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuizResultSummary {
    private String quizSessionId;
    private int totalQuestionCount;
    private int correctCount;
    private int incorrectCount;
    private int skipCount;
    private double accuracyRate;
}