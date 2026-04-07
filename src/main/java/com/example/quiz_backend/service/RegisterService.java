package com.example.quiz_backend.service;

import org.springframework.stereotype.Service;

import com.example.quiz_backend.dto.RegisterRequest;
import com.example.quiz_backend.dto.RegisterResponse;
import com.example.quiz_backend.entity.User;
import com.example.quiz_backend.repository.UserRepository;

@Service
public class RegisterService {

    private final UserRepository userRepository;

    public RegisterService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public RegisterResponse register(RegisterRequest request) {
        RegisterResponse response = new RegisterResponse();

        if (request == null) {
            response.setSuccess(false);
            response.setMessage("リクエストがありません。");
            return response;
        }

        String loginId = request.getLoginId() == null ? null : request.getLoginId().trim();
        String password = request.getPassword() == null ? null : request.getPassword().trim();

        if (loginId == null || loginId.isBlank() || password == null || password.isBlank()) {
            response.setSuccess(false);
            response.setMessage("ログインIDとパスワードを入力してください。");
            return response;
        }

        boolean isEmailLogin = isEmailFormat(loginId);

        if (userRepository.existsByUserId(loginId) || (isEmailLogin && userRepository.existsByEmail(loginId))) {
            response.setSuccess(false);
            response.setMessage("そのログインIDはすでに登録されています。");
            return response;
        }

        User user = new User();
        user.setUserId(loginId);
        user.setPassword(password);
        user.setName(loginId);
        user.setEmail(isEmailLogin ? loginId : null);

        userRepository.save(user);

        response.setSuccess(true);
        response.setMessage("ユーザー登録が完了しました。");
        return response;
    }

    private boolean isEmailFormat(String loginId) {
        return loginId != null && loginId.contains("@");
    }
}
