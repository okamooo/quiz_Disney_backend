package com.example.quiz_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.quiz_backend.entity.QuizSession;

public interface QuizSessionRepository extends JpaRepository<QuizSession, String> {
}