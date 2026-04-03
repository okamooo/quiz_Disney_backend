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
@Table(name = "choices")
@Data
public class Choice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 選択肢ID

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz; // クイズID

    @Column(name = "choice_text")
    private String choiceText; // 選択肢の文言

    @Column(name = "is_correct")
    private Boolean isCorrect; // 正解フラグ

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt; // 作成日時

    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // 更新日時
}
