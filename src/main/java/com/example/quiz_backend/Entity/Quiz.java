package com.example.quiz_backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "quizzes")
@Data
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // クイズID

    @Column(name = "category_id")
    private Long categoryId;

    @Column(columnDefinition = "TEXT")
    private String phrase; // フレーズ全文

    @Column(columnDefinition = "TEXT")
    private String translation; // フレーズの意味

    @Column(name = "question_word")
    private String questionWord; // 問題にする単語

    private String meaning; // 正しい意味

    @Column(name = "wrong_word1")
    private String wrongAnswer1; // 選択肢1

    @Column(name = "wrong_word2")
    private String wrongAnswer2; // 選択肢2

    @Column(name = "wrong_word3")
    private String wrongAnswer3; // 選択肢3

    @Column(columnDefinition = "TEXT")
    private String explanation; // 解説

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt; // 作成日時

    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // 更新日時
}
