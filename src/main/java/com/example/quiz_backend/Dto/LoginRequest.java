package com.example.quiz_backend.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String loginId; // メール / ユーザーID
    private String password; // パスワード
}
