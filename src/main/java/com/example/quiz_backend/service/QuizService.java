package com.example.quiz_backend.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.quiz_backend.dto.AnswerQuizRequest;
import com.example.quiz_backend.dto.AnswerQuizResponse;
import com.example.quiz_backend.dto.QuizChoice;
import com.example.quiz_backend.dto.QuizProgress;
import com.example.quiz_backend.dto.QuizQuestion;
import com.example.quiz_backend.dto.QuizResultSummary;
import com.example.quiz_backend.dto.StartQuizRequest;
import com.example.quiz_backend.dto.StartQuizResponse;
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

    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final QuizSessionRepository quizSessionRepository;
    private final QuizSessionAnswerRepository quizSessionAnswerRepository;

    @Transactional
    public StartQuizResponse startQuiz(StartQuizRequest request) {
        List<Quiz> quizzes = quizRepository.findByCategoryIdOrderByIdAsc(request.getCategoryId());
        if (quizzes.isEmpty()) {
            throw new RuntimeException("No quizzes found for category: " + request.getCategoryId());
        }

        User user = null;
        if (request.getUserId() != null && !request.getUserId().isBlank()) {
            user = userRepository.findByUserId(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found: " + request.getUserId()));
        }

        QuizSession session = new QuizSession();
        session.setId(UUID.randomUUID().toString());
        session.setUser(user);
        session.setCategoryId(request.getCategoryId());
        session.setQuizMode(
                request.getMode() == null || request.getMode().isBlank() ? DEFAULT_MODE : request.getMode());
        session.setTotalQuestionCount(quizzes.size());
        session.setCurrentQuestionNumber(1);
        session.setCorrectCount(0);
        session.setIncorrectCount(0);
        session.setSkipCount(0);
        session.setStatus(STATUS_IN_PROGRESS);
        session.setStartedAt(LocalDateTime.now());
        quizSessionRepository.save(session);

        Quiz firstQuiz = quizzes.get(0);

        return StartQuizResponse.builder()
                .quizSessionId(session.getId())
                .quizId(firstQuiz.getId())
                .progress(buildProgress(session))
                .question(buildQuestion(firstQuiz, 1, quizzes.size()))
                .build();
    }

    @Transactional
    public StartQuizResponse startQuizByCategory(Long categoryId) {
        StartQuizRequest request = new StartQuizRequest();
        request.setCategoryId(categoryId);
        request.setMode(DEFAULT_MODE);
        return startQuiz(request);
    }

    @Transactional
    public AnswerQuizResponse answerQuiz(String sessionId, AnswerQuizRequest request) {
        QuizSession session = quizSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Quiz session not found: " + sessionId));

        if (!STATUS_IN_PROGRESS.equals(session.getStatus())) {
            throw new RuntimeException("Quiz session is not in progress: " + sessionId);
        }

        List<Quiz> quizzes = quizRepository.findByCategoryIdOrderByIdAsc(session.getCategoryId());
        if (quizzes.isEmpty()) {
            throw new RuntimeException("No quizzes found for category: " + session.getCategoryId());
        }

        int currentQuestionNumber = session.getCurrentQuestionNumber();
        if (currentQuestionNumber < 1 || currentQuestionNumber > quizzes.size()) {
            throw new RuntimeException("Invalid current question number: " + currentQuestionNumber);
        }

        Quiz currentQuiz = quizzes.get(currentQuestionNumber - 1);
        if (!currentQuiz.getId().equals(request.getQuestionId())) {
            throw new RuntimeException("Question does not match the current quiz session state");
        }

        String action = normalizeAction(request.getAction());
        boolean skipped = ACTION_SKIP.equals(action);
        String selectedChoiceText = skipped ? null : request.getSelectedChoiceText();
        if (!skipped && (selectedChoiceText == null || selectedChoiceText.isBlank())) {
            throw new RuntimeException("selectedChoiceText is required when action is ANSWER");
        }

        boolean isCorrect = !skipped && currentQuiz.getQuestionWord().equals(selectedChoiceText);

        QuizSessionAnswer answer = new QuizSessionAnswer();
        answer.setQuizSession(session);
        answer.setQuiz(currentQuiz);
        answer.setQuestionOrder(currentQuestionNumber);
        answer.setSelectedChoiceText(selectedChoiceText);
        answer.setCorrectChoiceText(currentQuiz.getQuestionWord());
        answer.setIsCorrect(skipped ? null : isCorrect);
        answer.setIsSkipped(skipped);
        answer.setAnsweredAt(LocalDateTime.now());
        quizSessionAnswerRepository.save(answer);

        if (skipped) {
            session.setSkipCount(session.getSkipCount() + 1);
        } else if (isCorrect) {
            session.setCorrectCount(session.getCorrectCount() + 1);
        } else {
            session.setIncorrectCount(session.getIncorrectCount() + 1);
        }

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

        Quiz nextQuiz = quizzes.get(nextQuestionNumber - 1);

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
        int answeredCount = session.getCorrectCount() + session.getIncorrectCount() + session.getSkipCount();
        int currentQuestionNumber = Math.min(session.getCurrentQuestionNumber(), session.getTotalQuestionCount());

        return QuizProgress.builder()
                .currentQuestionNumber(currentQuestionNumber)
                .totalQuestionCount(session.getTotalQuestionCount())
                .answeredCount(answeredCount)
                .correctCount(session.getCorrectCount())
                .incorrectCount(session.getIncorrectCount())
                .skipCount(session.getSkipCount())
                .build();
    }

    private QuizResultSummary buildResultSummary(QuizSession session) {
        double accuracyRate = session.getTotalQuestionCount() == 0
                ? 0.0
                : (double) session.getCorrectCount() / session.getTotalQuestionCount() * 100;

        return QuizResultSummary.builder()
                .quizSessionId(session.getId())
                .totalQuestionCount(session.getTotalQuestionCount())
                .correctCount(session.getCorrectCount())
                .incorrectCount(session.getIncorrectCount())
                .skipCount(session.getSkipCount())
                .accuracyRate(accuracyRate)
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

        throw new RuntimeException("Unsupported action: " + action);
    }
}