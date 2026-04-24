package com.example.quiz_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.quiz_backend.dto.HomeResponse;
import com.example.quiz_backend.service.HomeService;

/**
 * ホーム画面のコントローラー
 * ここでは、ホーム画面に必要なAPIエンドポイントを定義します
 * 
 * @author Takuya Okamoto
 */
@RestController
@RequestMapping("/api/v1/quiz")
// @CrossOrigin(origins = "http://localhost:5173") // フロントエンドからのアクセスを許可
@CrossOrigin(origins = { "http://localhost:5173", "http://3.112.81.98" }) // フロントエンドからのアクセスを許可
public class HomeController {
    private final HomeService homeService;

    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping("/home")
    public ResponseEntity<HomeResponse> getHome(@RequestParam(required = false) String loginId) {

        HomeResponse response = homeService.getHomeData(loginId);
        if (response.getUserId() == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(response);
    }

}
