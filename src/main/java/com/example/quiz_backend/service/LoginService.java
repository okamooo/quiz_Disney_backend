package com.example.quiz_backend.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.quiz_backend.dto.LoginRequest;
import com.example.quiz_backend.dto.LoginResponse;
import com.example.quiz_backend.entity.User;
import com.example.quiz_backend.repository.UserRepository;

@Service
public class LoginService {

    private final UserRepository userRepository;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LoginResponse login(LoginRequest request) {
        LoginResponse response = new LoginResponse();

        if (request == null) {
            response.setSuccess(false);
            response.setMessage("リクエストがありません。");
            return response;
        }

        String loginId = request.getLoginId() == null ? null : request.getLoginId().trim();
        String password = request.getPassword() == null ? null : request.getPassword().trim();

        if (loginId == null || loginId.isBlank() || password == null || password.isBlank()) {
            response.setSuccess(false);
            response.setMessage("ログインIDとパスワードは必須です。");
            return response;
        }

        Optional<User> user = findUserByLoginId(loginId);

        if (user.isPresent() && password.equals(user.get().getPassword())) {
            response.setSuccess(true);
            response.setMessage("ログインに成功しました。");
        } else {
            response.setSuccess(false);
            response.setMessage("ログインIDまたはパスワードが正しくありません。");
        }

        return response;
    }

    private Optional<User> findUserByLoginId(String loginId) {
        if (isEmailFormat(loginId)) {
            return userRepository.findByEmail(loginId);
        }

        return userRepository.findByUserId(loginId);
    }

    private boolean isEmailFormat(String loginId) {
        return loginId != null && loginId.contains("@");
    }
}
