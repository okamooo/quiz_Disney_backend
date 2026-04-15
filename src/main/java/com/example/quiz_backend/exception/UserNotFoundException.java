package com.example.quiz_backend.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String userId) {
        super("ユーザーが見つかりません。userId: " + userId);
    }
}
