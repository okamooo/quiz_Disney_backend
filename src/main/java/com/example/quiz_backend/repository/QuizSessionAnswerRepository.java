package com.example.quiz_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.quiz_backend.entity.QuizSessionAnswer;

public interface QuizSessionAnswerRepository extends JpaRepository<QuizSessionAnswer, Long> {
    List<QuizSessionAnswer> findByQuizSessionIdOrderByQuestionOrderAsc(String quizSessionId);
}