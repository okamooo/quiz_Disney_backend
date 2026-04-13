package com.example.quiz_backend.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.quiz_backend.dto.StartQuizResponse;
import com.example.quiz_backend.service.QuizService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/quiz")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @PostMapping("/start/category/{categoryId}")
    public StartQuizResponse startQuizByCategory(@PathVariable Long categoryId) {
        return quizService.startQuizByCategory(categoryId);
    }
}
