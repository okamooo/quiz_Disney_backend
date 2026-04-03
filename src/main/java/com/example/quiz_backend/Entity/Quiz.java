package com.example.quiz_backend.Entity;

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

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category; // カテゴリID

    @Column(columnDefinition = "TEXT")
    private String phrase; // フレーズ全文

    @Column(name = "question_word")
    private String questionWord; // 問題にする単語

    private String meaning; // 正しい意味

    @Column(columnDefinition = "TEXT")
    private String explanation; // 解説

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt; // 作成日時

    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // 更新日時
}