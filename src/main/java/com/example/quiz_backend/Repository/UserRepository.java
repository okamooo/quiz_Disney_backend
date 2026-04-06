package com.example.quiz_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.quiz_backend.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    // メールアドレスでユーザー取得（ログイン用）
    Optional<User> findByEmail(String email);

    // ログインIDでユーザー取得（ログイン用）
    Optional<User> findByUserId(String userId);

}
