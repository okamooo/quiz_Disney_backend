package com.example.quiz_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class QuizSessionNotFinishedException extends RuntimeException {
    public QuizSessionNotFinishedException(String sessionId) {
        super("未完了のクイズセッションのため結果を取得できません sessionId: " + sessionId);
    }
}