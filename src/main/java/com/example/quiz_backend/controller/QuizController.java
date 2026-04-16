package com.example.quiz_backend.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.quiz_backend.dto.AnswerQuizRequest;
import com.example.quiz_backend.dto.AnswerQuizResponse;
import com.example.quiz_backend.dto.QuizSessionResultResponse;
import com.example.quiz_backend.dto.StartQuizRequest;
import com.example.quiz_backend.dto.StartQuizResponse;
import com.example.quiz_backend.service.QuizService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/quiz")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @PostMapping("/start")
    public StartQuizResponse startQuiz(@RequestBody StartQuizRequest request) {
        return quizService.startQuiz(request);
    }

    @GetMapping("/{sessionId}/result")
    public QuizSessionResultResponse getQuizResult(@PathVariable String sessionId) {
        return quizService.getQuizResult(sessionId);
    }

    @PostMapping("/{sessionId}/answer")
    public AnswerQuizResponse answerQuiz(
            @PathVariable String sessionId,
            @RequestBody AnswerQuizRequest request) {
        return quizService.answerQuiz(sessionId, request);
    }
}