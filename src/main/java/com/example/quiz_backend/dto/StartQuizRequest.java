package com.example.quiz_backend.dto;

import lombok.Data;

@Data
public class StartQuizRequest {
    private String userId;
    private Long categoryId;
    private String mode;
}