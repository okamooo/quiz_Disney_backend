package com.example.quiz_backend.Dto;

import lombok.Data;

@Data
public class LoginResponse {
    private String userId; // ユーザー名 or メールアドレス
    private String messeage;
}
