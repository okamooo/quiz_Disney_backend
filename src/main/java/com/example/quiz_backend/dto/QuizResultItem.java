package com.example.quiz_backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuizResultItem {
    private int questionNumber;
    private String phraseText;
    private boolean isCorrect;
    private String correctWord;
}