package com.example.quiz_backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.quiz_backend.dto.HomeResponse;
import com.example.quiz_backend.dto.QuizMode;
import com.example.quiz_backend.entity.QuizResult;
import com.example.quiz_backend.entity.User;
import com.example.quiz_backend.repository.QuizResultRepository;
import com.example.quiz_backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * ホーム画面のサービスクラス
 * 
 * @author Takuya Okamoto
 */
@Service
@RequiredArgsConstructor
public class HomeService {

    private final UserRepository userRepository;
    private final QuizResultRepository quizResultRepository;

    public HomeResponse getHomeData(String loginId) {
        HomeResponse response = new HomeResponse();

        Optional<User> userOpt = userRepository.findByUserId(loginId);
        if (userOpt.isEmpty()) {
            return response; // ユーザーが見つからない場合は空のレスポンスを返す
        }
        User user = userOpt.get();

        response.setUserId(user.getUserId());
        response.setUserName(user.getName());

        List<QuizResult> quizResults = quizResultRepository.findByUserOrderByAnsweredAtDesc(user);

        int totalSolvedCount = quizResults.size();
        int correctCount = (int) quizResults.stream().filter(QuizResult::getIsCorrect).count();
        int incorrectCount = totalSolvedCount - correctCount;
        double accuracyRate = totalSolvedCount > 0 ? (double) correctCount / totalSolvedCount * 100 : 0.0;

        response.setTotalSolvedCount(totalSolvedCount);
        response.setCorrectCount(correctCount);
        response.setIncorrectCount(incorrectCount);
        response.setAccuracyRate(accuracyRate);

        if (!quizResults.isEmpty()) {
            response.setLastPlayedAt(quizResults.get(0).getAnsweredAt().toString());
        }

        response.setQuizModes(createDefaultQuizModes());
        response.setHasAvailableQuiz(true);

        return response;
    }

    private List<QuizMode> createDefaultQuizModes() {
        List<QuizMode> quizModes = new ArrayList<>();

        QuizMode mode1 = new QuizMode();
        mode1.setQuizMode("mode1");
        mode1.setQuizModeLabel("選択問題");
        mode1.setQuizStartUrl("/api/v1/quiz/start/select");
        quizModes.add(mode1);

        QuizMode mode2 = new QuizMode();
        mode2.setQuizMode("mode2");
        mode2.setQuizModeLabel("並べ替え問題");
        mode2.setQuizStartUrl("/api/v1/quiz/start/sort");
        quizModes.add(mode2);

        return quizModes;
    }

}
