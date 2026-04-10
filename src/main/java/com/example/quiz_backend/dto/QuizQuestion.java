package com.example.quiz_backend.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuizQuestion {
    private Long questionId;
    private int currentQuestionNumber;
    private int totalQuestionCount;
    private long categoryId;
    private String phraseText;
    private String targetWord;
    private List<QuizChoice> choices;
}
