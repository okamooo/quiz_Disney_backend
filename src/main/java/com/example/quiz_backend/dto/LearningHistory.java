package com.example.quiz_backend.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LearningHistory {
    private String historyId;
    private String playedAt;
    private int solvedCount;
    private int correctCount;
    private int incorrectCount;
    private List<LearningHistoryAnswer> answers;
}