package com.example.quiz_backend.dto;

import lombok.Data;

@Data
public class RegisterResponse {
    private boolean success;
    private String message;
}
