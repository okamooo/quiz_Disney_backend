package com.example.quiz_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class QuizMode {
    private String quizMode;
    private String quizModeLabel;
    private String quizStartUrl;

    @JsonProperty("isAvailable") // JSONのキーを "isAvailable" にする
    private boolean isAvailable; // クイズが利用可能かどうか
}
