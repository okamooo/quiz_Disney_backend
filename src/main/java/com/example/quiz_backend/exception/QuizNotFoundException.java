package com.example.quiz_backend.exception;

public class QuizNotFoundException extends RuntimeException {
    public QuizNotFoundException(Long quizId) {
        super("クイズが見つかりません。quizId: " + quizId);
    }
}
