package com.example.quiz_backend.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private boolean success;
    private String message;
}
