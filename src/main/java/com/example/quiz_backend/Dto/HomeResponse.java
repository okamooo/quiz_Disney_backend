package com.example.quiz_backend.dto;

import java.util.List;

import lombok.Data;

@Data
public class HomeResponse {
    private String userId;
    private String userName;
    private String welcomeMessage;
    private List<QuizMode> quizModes;
    private QuizMode selectedQuizMode;
    private boolean hasAvailableQuiz;
    private List<LearningHistory> learningHistories;
    private int totalSolvedCount;
    private int correctCount;
    private int incorrectCount;
    private String lastPlayedAt;
    private double accuracyRate;
}
