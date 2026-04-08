package com.example.quiz_backend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/quiz")
@CrossOrigin(origins = "http://localhost:5173")
// フロントエンドからのアクセスを許可
public class QuizConrtroller {

    @GetMapping("/test")
    public Map<String, Object> getTestQuiz() {
        Map<String, Object> quiz = new HashMap<>();
        quiz.put("id", 1);
        quiz.put("question", "Javaで「不変の」を意味する単語は？");
        quiz.put("answer", "Immutable");
        return quiz;
    }
}
