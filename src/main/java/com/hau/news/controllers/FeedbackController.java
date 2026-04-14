package com.hau.news.controllers;

import com.hau.news.serviceimpls.FeedbackTokenServiceImpl;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
@RestController
@RequestMapping("/feedback")
public class FeedbackController {
    private final FeedbackTokenServiceImpl feedbackTokenService;
    @Autowired
    public FeedbackController(FeedbackTokenServiceImpl feedbackTokenService){
        this.feedbackTokenService = feedbackTokenService;
    }
/*    @GetMapping("/feedback")
    public ResponseEntity<?> openFeedback(@RequestParam String token) {

        FeedbackTokenServiceImpl.TokenPayload payload = feedbackTokenService.validateToken(token);

        return ResponseEntity.ok(
                Map.of(
                        "userId", payload.userId(),
                        "articleOd", payload.articleOid()
                )
        );
    }*/
    @PostMapping("/feedback/like")
    public ResponseEntity<?> like(@RequestParam String token) {

        FeedbackTokenServiceImpl.TokenPayload payload = feedbackTokenService.validateToken(token);

        feedbackTokenService.saveLike(payload.userId(), payload.articleOid());

        return ResponseEntity.ok("Liked!");
    }

}
