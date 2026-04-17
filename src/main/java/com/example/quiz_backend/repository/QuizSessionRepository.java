package com.example.quiz_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.quiz_backend.entity.QuizSession;
import com.example.quiz_backend.entity.User;

public interface QuizSessionRepository extends JpaRepository<QuizSession, String> {
    List<QuizSession> findByUserAndStatusOrderByStartedAtDesc(User user, String status);
}