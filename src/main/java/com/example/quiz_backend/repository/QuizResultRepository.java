package com.example.quiz_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.quiz_backend.entity.QuizResult;
import com.example.quiz_backend.entity.User;

public interface QuizResultRepository extends JpaRepository<QuizResult, Long> {
    List<QuizResult> findByUserOrderByAnsweredAtDesc(User user);
}
