package com.example.quiz_backend.service;

import org.springframework.stereotype.Service;

import com.example.quiz_backend.dto.LoginRequest;
import com.example.quiz_backend.dto.LoginResponse;

@Service
public class LoginService {

    private static final String DEMO_EMAIL = "test@example.com";
    private static final String DEMO_USER_ID = "testuser";
    private static final String DEMO_PASSWORD = "password123";

    public LoginResponse login(LoginRequest request) {
        LoginResponse response = new LoginResponse();

        if (request == null) {
            response.setSuccess(false);
            response.setMessage("リクエストがありません。");
            return response;
        }

        String loginId = request.getLoginId() == null ? null : request.getLoginId().trim();
        String password = request.getPassword();

        if (loginId == null || loginId.isBlank() || password == null || password.isBlank()) {
            response.setSuccess(false);
            response.setMessage("ログインIDとパスワードは必須です。");
            return response;
        }

        boolean isEmailLogin = isEmailFormat(loginId);
        boolean isMatched;

        if (isEmailLogin) {
            isMatched = DEMO_EMAIL.equals(loginId)
                    && DEMO_PASSWORD.equals(password);
        } else {
            isMatched = DEMO_USER_ID.equals(loginId)
                    && DEMO_PASSWORD.equals(password);
        }

        if (isMatched) {
            response.setSuccess(true);
            response.setMessage("ログインに成功しました。");
        } else {
            response.setSuccess(false);
            response.setMessage("ログインIDまたはパスワードが正しくありません。");
        }

        return response;
    }

    private boolean isEmailFormat(String loginId) {
        return loginId != null && loginId.contains("@");
    }
}
