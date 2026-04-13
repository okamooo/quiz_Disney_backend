package com.example.quiz_backend.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.quiz_backend.dto.QuizChoice;
import com.example.quiz_backend.dto.QuizProgress;
import com.example.quiz_backend.dto.QuizQuestion;
import com.example.quiz_backend.dto.StartQuizResponse;
import com.example.quiz_backend.entity.Quiz;
import com.example.quiz_backend.repository.QuizRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizRepository quizRepository;

    public StartQuizResponse startQuizByCategory(Long categoryId) {
        List<Quiz> quizzes = quizRepository.findByCategoryId(categoryId);
        if (quizzes.isEmpty()) {
            throw new RuntimeException("No quizzes found for category: " + categoryId);
        }

        // カテゴリ内の最初の問題を選択
        Quiz quiz = quizzes.get(0);

        List<QuizChoice> choises = new ArrayList<>();
        choises.add(new QuizChoice(0L, quiz.getQuestionWord()));
        choises.add(new QuizChoice(1L, quiz.getWrongAnswer1()));
        choises.add(new QuizChoice(2L, quiz.getWrongAnswer2()));
        choises.add(new QuizChoice(3L, quiz.getWrongAnswer3()));

        Collections.shuffle(choises);

        QuizQuestion question = QuizQuestion.builder()
                .questionId(quiz.getId())
                .currentQuestionNumber(1)
                .totalQuestionCount(quizzes.size())
                .categoryId(quiz.getCategoryId())
                .phraseText(quiz.getTranslation())
                .targetWord(quiz.getQuestionWord())
                .choices(choises)
                .build();

        QuizProgress progress = QuizProgress.builder()
                .currentQuestionNumber(1)
                .totalQuestionCount(quizzes.size())
                .answeredCount(0)
                .correctCount(0)
                .incorrectCount(0)
                .build();

        return StartQuizResponse.builder()
                .quizSessionId("dummy-session-id")
                .quizId(quiz.getId())
                .progress(progress)
                .question(question)
                .build();
    }
}
