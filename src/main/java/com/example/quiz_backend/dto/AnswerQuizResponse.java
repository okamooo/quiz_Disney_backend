package com.example.quiz_backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnswerQuizResponse {
    private String action;
    private boolean skipped;
    private Long questionId;
    private boolean correct;
    private String selectedChoiceText;
    private String translationText;
    private String correctAnswer;
    private String explanation;
    private QuizProgress progress;
    private QuizQuestion nextQuestion;
    private boolean finished;
    private QuizResultSummary result;
}