package com.example.quiz_backend.exception;

public class QuizSessionNotFoundException extends RuntimeException {
    public QuizSessionNotFoundException(String sessionId) {
        super("クイズセッションが見つかりません。sessionId: " + sessionId);
    }
}
