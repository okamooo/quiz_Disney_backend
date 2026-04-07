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

        String userId = request.getUserId() == null ? null : request.getUserId().trim();
        String email = request.getEmail() == null ? null : request.getEmail().trim();
        String password = request.getPassword() == null ? null : request.getPassword().trim();

        if (userId == null || userId.isBlank() || email == null || email.isBlank()
                || password == null || password.isBlank()) {
            response.setSuccess(false);
            response.setMessage("ログインIDとパスワードを入力してください。");
            return response;
        }

        if (!isEmailFormat(email)) {
            response.setSuccess(false);
            response.setMessage("メールアドレスの形式が正しくありません。");
            return response;
        }

        if (userRepository.existsByUserId(userId)) {
            response.setSuccess(false);
            response.setMessage("そのログインIDはすでに登録されています。");
            return response;
        }

        if (userRepository.existsByEmail(email)) {
            response.setSuccess(false);
            response.setMessage("そのメールアドレスはすでに登録されています。");
            return response;
        }

        User user = new User();
        user.setUserId(userId);
        user.setPassword(password);
        user.setName(userId);
        user.setEmail(email);

        userRepository.save(user);

        response.setSuccess(true);
        response.setMessage("ユーザー登録が完了しました。");
        return response;
    }

    private boolean isEmailFormat(String loginId) {
        return loginId != null && loginId.contains("@");
    }
}
