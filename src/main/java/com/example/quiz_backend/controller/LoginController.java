package com.example.quiz_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.quiz_backend.Dto.LoginRequest;
import com.example.quiz_backend.Repository.UserRepository;

@RestController
@RequestMapping("/api/v1/quiz")
@CrossOrigin(origins = "http://localhost:5173")
// ログイン関連のエンドポイントをここに実装予定
public class LoginController {

    @AutoWired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<Loginresponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(new Loginresponse());
    }
}