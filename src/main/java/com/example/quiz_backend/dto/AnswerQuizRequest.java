package com.example.quiz_backend.dto;

import lombok.Data;

@Data
public class AnswerQuizRequest {
    private String action;
    private Long questionId;
    private String selectedChoiceText;
}