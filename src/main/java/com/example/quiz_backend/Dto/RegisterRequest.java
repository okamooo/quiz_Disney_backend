package com.example.quiz_backend.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String userId;
    private String email;
    private String password;
}
