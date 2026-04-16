package com.example.quiz_backend.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Locale;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.quiz_backend.dto.AnswerQuizRequest;
import com.example.quiz_backend.dto.AnswerQuizResponse;
import com.example.quiz_backend.dto.QuizChoice;
import com.example.quiz_backend.dto.QuizProgress;
import com.example.quiz_backend.dto.QuizQuestion;
import com.example.quiz_backend.dto.QuizResultItem;
import com.example.quiz_backend.dto.QuizResultSummary;
import com.example.quiz_backend.dto.QuizSessionResultResponse;
import com.example.quiz_backend.dto.StartQuizRequest;
import com.example.quiz_backend.dto.StartQuizResponse;
import com.example.quiz_backend.exception.QuizNotFoundException;
import com.example.quiz_backend.exception.QuizSessionNotFinishedException;
import com.example.quiz_backend.exception.QuizSessionNotFoundException;
import com.example.quiz_backend.exception.UserNotFoundException;
import com.example.quiz_backend.entity.Quiz;
import com.example.quiz_backend.entity.QuizSession;
import com.example.quiz_backend.entity.QuizSessionAnswer;
import com.example.quiz_backend.entity.User;
import com.example.quiz_backend.repository.QuizRepository;
import com.example.quiz_backend.repository.QuizSessionAnswerRepository;
import com.example.quiz_backend.repository.QuizSessionRepository;
import com.example.quiz_backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuizService {

    private static final String STATUS_IN_PROGRESS = "IN_PROGRESS";
    private static final String STATUS_FINISHED = "FINISHED";
    private static final String DEFAULT_MODE = "choice";
    private static final String ACTION_ANSWER = "ANSWER";
    private static final String ACTION_SKIP = "SKIP";
    private static final int SESSION_QUESTION_COUNT = 10;

    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final QuizSessionRepository quizSessionRepository;
    private final QuizSessionAnswerRepository quizSessionAnswerRepository;

    @Transactional
    public StartQuizResponse startQuiz(StartQuizRequest request) {
        List<Quiz> quizzes = new ArrayList<>(quizRepository.findByCategoryId(request.getCategoryId()));
        if (quizzes.size() < SESSION_QUESTION_COUNT) {
            throw new IllegalStateException(
                    "カテゴリID: " + request.getCategoryId() +
                            " は最低 " + SESSION_QUESTION_COUNT + " 問必要ですが、現在は " +
                            quizzes.size() + " 問しかありません");
        }

        Collections.shuffle(quizzes);
        List<Quiz> sessionQuizzes = new ArrayList<>(quizzes.subList(0, SESSION_QUESTION_COUNT));

        User user = null;
        if (request.getUserId() != null && !request.getUserId().isBlank()) {
            user = userRepository.findByUserId(request.getUserId())
                    .orElseThrow(() -> new UserNotFoundException(request.getUserId()));
        }

        QuizSession session = new QuizSession();
        session.setId(UUID.randomUUID().toString());
        session.setUser(user);
        session.setCategoryId(request.getCategoryId());
        session.setQuizMode(
                request.getMode() == null || request.getMode().isBlank() ? DEFAULT_MODE : request.getMode());
        session.setQuestionIds(sessionQuizzes.stream()
                .map(Quiz::getId)
                .collect(Collectors.toCollection(ArrayList::new)));
        session.setTotalQuestionCount(sessionQuizzes.size());
        session.setCurrentQuestionNumber(1);
        session.setCorrectCount(0);
        session.setIncorrectCount(0);
        session.setStatus(STATUS_IN_PROGRESS);
        session.setStartedAt(LocalDateTime.now());
        quizSessionRepository.save(session);

        Quiz firstQuiz = sessionQuizzes.get(0);

        return StartQuizResponse.builder()
                .quizSessionId(session.getId())
                .quizId(firstQuiz.getId())
                .progress(buildProgress(session))
                .question(buildQuestion(firstQuiz, 1, sessionQuizzes.size()))
                .build();
    }

    @Transactional
    public AnswerQuizResponse answerQuiz(String sessionId, AnswerQuizRequest request) {
        QuizSession session = quizSessionRepository.findById(sessionId)
                .orElseThrow(() -> new QuizSessionNotFoundException(sessionId));

        if (!STATUS_IN_PROGRESS.equals(session.getStatus())) {
            throw new IllegalStateException("クイズセッションは進行中ではありません。sessionId: " + sessionId);
        }

        List<Long> questionIds = session.getQuestionIds();
        if (questionIds == null || questionIds.isEmpty()) {
            throw new IllegalStateException("クイズセッションに出題対象の問題が設定されていません。sessionId: " + sessionId);
        }

        int currentQuestionNumber = session.getCurrentQuestionNumber();
        if (currentQuestionNumber < 1 || currentQuestionNumber > questionIds.size()) {
            throw new IllegalStateException("現在の問題番号が不正です。currentQuestionNumber: " + currentQuestionNumber);
        }

        Quiz currentQuiz = findQuizOrThrow(questionIds.get(currentQuestionNumber - 1));
        if (!currentQuiz.getId().equals(request.getQuestionId())) {
            throw new IllegalStateException("リクエストされた問題が現在のクイズセッション状態と一致しません。");
        }

        String action = normalizeAction(request.getAction());
        boolean skipped = ACTION_SKIP.equals(action);
        String selectedChoiceText = skipped ? null : request.getSelectedChoiceText();
        if (!skipped && (selectedChoiceText == null || selectedChoiceText.isBlank())) {
            throw new IllegalArgumentException("回答時は selectedChoiceText が必須です。");
        }

        boolean isCorrect = !skipped && currentQuiz.getQuestionWord().equals(selectedChoiceText);

        QuizSessionAnswer answer = new QuizSessionAnswer();
        answer.setQuizSession(session);
        answer.setQuiz(currentQuiz);
        answer.setQuestionOrder(currentQuestionNumber);
        answer.setSelectedChoiceText(selectedChoiceText);
        answer.setCorrectChoiceText(currentQuiz.getQuestionWord());
        answer.setIsCorrect(isCorrect);
        answer.setIsSkipped(skipped);
        answer.setAnsweredAt(LocalDateTime.now());
        quizSessionAnswerRepository.save(answer);

        updateAnswerCounts(session, skipped, isCorrect);

        int nextQuestionNumber = currentQuestionNumber + 1;
        boolean finished = nextQuestionNumber > session.getTotalQuestionCount();

        if (finished) {
            session.setStatus(STATUS_FINISHED);
            session.setFinishedAt(LocalDateTime.now());
            session.setCurrentQuestionNumber(session.getTotalQuestionCount());
            quizSessionRepository.save(session);

            return AnswerQuizResponse.builder()
                    .action(action)
                    .skipped(skipped)
                    .questionId(currentQuiz.getId())
                    .correct(isCorrect)
                    .selectedChoiceText(selectedChoiceText)
                    .translationText(currentQuiz.getTranslation())
                    .correctAnswer(currentQuiz.getQuestionWord())
                    .explanation(currentQuiz.getExplanation())
                    .progress(buildProgress(session))
                    .finished(true)
                    .result(buildResultSummary(session))
                    .build();
        }

        session.setCurrentQuestionNumber(nextQuestionNumber);
        quizSessionRepository.save(session);

        Quiz nextQuiz = findQuizOrThrow(questionIds.get(nextQuestionNumber - 1));

        return AnswerQuizResponse.builder()
                .action(action)
                .skipped(skipped)
                .questionId(currentQuiz.getId())
                .correct(isCorrect)
                .selectedChoiceText(selectedChoiceText)
                .translationText(currentQuiz.getTranslation())
                .correctAnswer(currentQuiz.getQuestionWord())
                .explanation(currentQuiz.getExplanation())
                .progress(buildProgress(session))
                .nextQuestion(buildQuestion(nextQuiz, nextQuestionNumber, session.getTotalQuestionCount()))
                .finished(false)
                .build();
    }

    @Transactional(readOnly = true)
    public QuizSessionResultResponse getQuizResult(String sessionId) {
        QuizSession session = quizSessionRepository.findById(sessionId)
                .orElseThrow(() -> new QuizSessionNotFoundException(sessionId));

        if (!STATUS_FINISHED.equals(session.getStatus())) {
            throw new QuizSessionNotFinishedException(sessionId);
        }

        List<QuizResultItem> results = quizSessionAnswerRepository.findByQuizSessionIdOrderByQuestionOrderAsc(sessionId)
                .stream()
                .map(answer -> QuizResultItem.builder()
                        .questionNumber(answer.getQuestionOrder())
                        .phraseText(answer.getQuiz().getPhrase())
                        .isCorrect(answer.getIsCorrect())
                        .correctWord(answer.getCorrectChoiceText())
                        .build())
                .collect(Collectors.toList());

        return QuizSessionResultResponse.builder()
                .totalQuestionCount(session.getTotalQuestionCount())
                .correctCount(session.getCorrectCount())
                .results(results)
                .build();
    }

    private void updateAnswerCounts(QuizSession session, boolean skipped, boolean isCorrect) {
        if (skipped) {
            session.setIncorrectCount(session.getIncorrectCount() + 1);
        } else if (isCorrect) {
            session.setCorrectCount(session.getCorrectCount() + 1);
        } else {
            session.setIncorrectCount(session.getIncorrectCount() + 1);
        }
    }

    QuizQuestion buildQuestion(Quiz quiz, int currentQuestionNumber, int totalQuestionCount) {
        List<QuizChoice> choices = new ArrayList<>();
        choices.add(new QuizChoice(0L, quiz.getQuestionWord()));
        choices.add(new QuizChoice(1L, quiz.getWrongAnswer1()));
        choices.add(new QuizChoice(2L, quiz.getWrongAnswer2()));
        choices.add(new QuizChoice(3L, quiz.getWrongAnswer3()));
        Collections.shuffle(choices);

        return QuizQuestion.builder()
                .questionId(quiz.getId())
                .currentQuestionNumber(currentQuestionNumber)
                .totalQuestionCount(totalQuestionCount)
                .categoryId(quiz.getCategoryId())
                .phraseText(quiz.getPhrase())
                .targetWord(quiz.getQuestionWord())
                .choices(choices)
                .build();
    }

    QuizProgress buildProgress(QuizSession session) {
        int answeredCount = session.getCorrectCount() + session.getIncorrectCount();
        int currentQuestionNumber = Math.min(session.getCurrentQuestionNumber(), session.getTotalQuestionCount());

        return QuizProgress.builder()
                .currentQuestionNumber(currentQuestionNumber)
                .totalQuestionCount(session.getTotalQuestionCount())
                .answeredCount(answeredCount)
                .correctCount(session.getCorrectCount())
                .incorrectCount(session.getIncorrectCount())
                .build();
    }

    private Quiz findQuizOrThrow(Long quizId) {
        return quizRepository.findById(quizId)
                .orElseThrow(() -> new QuizNotFoundException(quizId));
    }

    private QuizResultSummary buildResultSummary(QuizSession session) {
        return QuizResultSummary.builder()
                .quizSessionId(session.getId())
                .totalQuestionCount(session.getTotalQuestionCount())
                .correctCount(session.getCorrectCount())
                .incorrectCount(session.getIncorrectCount())
                .build();
    }

    private String normalizeAction(String action) {
        if (action == null || action.isBlank()) {
            return ACTION_ANSWER;
        }

        String normalized = action.toUpperCase(Locale.ROOT);
        if (ACTION_ANSWER.equals(normalized) || ACTION_SKIP.equals(normalized)) {
            return normalized;
        }

        throw new IllegalArgumentException("未対応のアクションです。action: " + action);
    }
}