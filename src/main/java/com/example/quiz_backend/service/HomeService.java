package com.example.quiz_backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.quiz_backend.dto.HomeResponse;
import com.example.quiz_backend.dto.LearningHistory;
import com.example.quiz_backend.dto.LearningHistoryAnswer;
import com.example.quiz_backend.dto.QuizMode;
import com.example.quiz_backend.entity.QuizSession;
import com.example.quiz_backend.entity.QuizSessionAnswer;
import com.example.quiz_backend.entity.User;
import com.example.quiz_backend.repository.QuizSessionAnswerRepository;
import com.example.quiz_backend.repository.QuizSessionRepository;
import com.example.quiz_backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HomeService {

    private static final String STATUS_FINISHED = "FINISHED";

    private final UserRepository userRepository;
    private final QuizSessionRepository quizSessionRepository;
    private final QuizSessionAnswerRepository quizSessionAnswerRepository;

    public HomeResponse getHomeData(String loginId) {
        HomeResponse response = new HomeResponse();

        Optional<User> userOpt = userRepository.findByUserId(loginId);
        if (userOpt.isEmpty()) {
            return response;
        }
        User user = userOpt.get();

        response.setUserId(user.getUserId());
        response.setUserName(user.getName());

        List<QuizSession> sessions = quizSessionRepository.findByUserAndStatusOrderByStartedAtDesc(user,
                STATUS_FINISHED);
        List<LearningHistory> learningHistories = new ArrayList<>();

        for (QuizSession session : sessions) {
            int sessionSolvedCount = defaultInt(session.getTotalQuestionCount());
            int sessionCorrectCount = defaultInt(session.getCorrectCount());
            int sessionIncorrectCount = defaultInt(session.getIncorrectCount());

            List<LearningHistoryAnswer> answers = quizSessionAnswerRepository
                    .findByQuizSessionIdOrderByQuestionOrderAsc(session.getId())
                    .stream()
                    .map(this::buildLearningHistoryAnswer)
                    .toList();

            learningHistories.add(LearningHistory.builder()
                    .historyId(session.getId())
                    .playedAt(session.getStartedAt().toString())
                    .solvedCount(sessionSolvedCount)
                    .correctCount(sessionCorrectCount)
                    .incorrectCount(sessionIncorrectCount)
                    .answers(answers)
                    .build());
        }

        response.setLearningHistories(learningHistories);
        response.setQuizModes(createDefaultQuizModes());
        response.setHasAvailableQuiz(true);

        return response;
    }

    private LearningHistoryAnswer buildLearningHistoryAnswer(QuizSessionAnswer answer) {
        return LearningHistoryAnswer.builder()
                .questionNumber(defaultInt(answer.getQuestionOrder()))
                .phraseText(answer.getQuiz().getPhrase())
                .correct(Boolean.TRUE.equals(answer.getIsCorrect()))
                .correctWord(answer.getCorrectChoiceText())
                .build();
    }

    private int defaultInt(Integer value) {
        return value != null ? value : 0;
    }

    private List<QuizMode> createDefaultQuizModes() {
        List<QuizMode> quizModes = new ArrayList<>();

        QuizMode mode1 = new QuizMode();
        mode1.setQuizMode("mode1");
        mode1.setQuizModeLabel("選択問題");
        mode1.setQuizStartUrl("/api/v1/quiz/start/select");
        mode1.setAvailable(true);
        quizModes.add(mode1);

        QuizMode mode2 = new QuizMode();
        mode2.setQuizMode("mode2");
        mode2.setQuizModeLabel("並べ替え問題");
        mode2.setQuizStartUrl("/api/v1/quiz/start/sort");
        mode2.setAvailable(false);
        quizModes.add(mode2);

        return quizModes;
    }
}