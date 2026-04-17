package com.example.quiz_backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LearningHistoryAnswer {
    private int questionNumber;
    private String phraseText;
    private boolean correct;
    private String correctWord;
}