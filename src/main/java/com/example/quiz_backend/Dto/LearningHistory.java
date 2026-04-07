package com.example.quiz_backend.dto;

import lombok.Data;

@Data
public class LearningHistory {
    private String historyId;
    private String playedAt;
    private int solvedCount;
    private int correctCount;
    private int incorrectCount;
    private double accuracyRate;
}
