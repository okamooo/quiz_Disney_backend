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
@Table(name = "quiz_results")
@Data
public class QuizResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 回答結果ID

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // ユーザーID

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz; // クイズID

    @ManyToOne
    @JoinColumn(name = "selected_choice_id")
    private Choice selectedChoice; // 選んだ選択肢ID

    @Column(name = "is_correct")
    private Boolean isCorrect; // 正解フラグ

    @Column(name = "answered_at")
    private LocalDateTime answeredAt; // 回答日時

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt; // 作成日時

    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // 更新日時
}