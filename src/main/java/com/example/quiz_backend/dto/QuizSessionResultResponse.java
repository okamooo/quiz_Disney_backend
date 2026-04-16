package com.example.quiz_backend.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuizSessionResultResponse {
    private int totalQuestionCount;
    private int correctCount;
    private List<QuizResultItem> results;
}