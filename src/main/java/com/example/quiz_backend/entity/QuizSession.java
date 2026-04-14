package com.example.quiz_backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "quiz_sessions")
@Data
public class QuizSession {
    @Id
    @Column(length = 64)
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "quiz_mode")
    private String quizMode;

    @Column(name = "total_question_count")
    private Integer totalQuestionCount;

    @Column(name = "current_question_number")
    private Integer currentQuestionNumber;

    @Column(name = "correct_count")
    private Integer correctCount;

    @Column(name = "incorrect_count")
    private Integer incorrectCount;

    @Column(name = "skip_count")
    private Integer skipCount;

    @Column(name = "status")
    private String status;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "finished_at")
    private LocalDateTime finishedAt;
}