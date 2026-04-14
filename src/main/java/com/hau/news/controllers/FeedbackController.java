package com.hau.news.controllers;

import com.hau.news.models.Article;
import com.hau.news.models.exceptions.ExpiredTokenException;
import com.hau.news.models.exceptions.InvalidTokenException;
import com.hau.news.repositories.FeedbackRepository;
import com.hau.news.responsebodies.ArticleResponseBody;
import com.hau.news.serviceimpls.FeedbackTokenServiceImpl;
import com.hau.news.services.ArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
@Controller
@RequestMapping("/feedback")
public class FeedbackController {

    private static final Logger logger = LoggerFactory.getLogger(FeedbackController.class);

    private final FeedbackTokenServiceImpl feedbackTokenService;
    private final ArticleService articleService;
    private final FeedbackRepository feedbackRepository;

    @Autowired
    public FeedbackController(
            FeedbackTokenServiceImpl feedbackTokenService,
            ArticleService articleService,
            FeedbackRepository feedbackRepository) {
        this.feedbackTokenService = feedbackTokenService;
        this.articleService = articleService;
        this.feedbackRepository = feedbackRepository;
    }

    /**
     * GET /feedback/like?token=XXX
     * Serves the HTML feedback page
     */
    @GetMapping("/like")
    public String openFeedbackPage(@RequestParam String token) {
        try {
            logger.info("Opening feedback page with token");
            feedbackTokenService.validateToken(token);
            return "feedback";
        } catch (Exception e) {
            logger.warn("Invalid token provided: {}", e.getMessage());
            return "feedback";
        }
    }

    /**
     * REST API: GET /feedback/article?token=XXX
     * Returns article details as JSON
     */
    @GetMapping("/article")
    @ResponseBody
    public ResponseEntity<?> getArticleForFeedback(@RequestParam String token) {
        try {
            logger.info("Fetching article details for feedback with token");

            FeedbackTokenServiceImpl.TokenPayload payload = feedbackTokenService.validateToken(token);
            ArticleResponseBody article = articleService.getArticleByOid(payload.articleOid());

            Map<String, Object> response = Map.of(
                    "userId", payload.userId(),
                    "articleOid", payload.articleOid(),
                    "articleTitle", article.getTitle(),
                    "articleContent", article.getContent(),
                    "token", token
            );

            logger.info("Article details fetched successfully");
            return ResponseEntity.ok(response);

        } catch (InvalidTokenException e) {
            logger.warn("Invalid token: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid token. Token may have been tampered with."));
        } catch (ExpiredTokenException e) {
            logger.warn("Expired token: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Token has expired. Please request a new newsletter."));
        } catch (Exception e) {
            logger.error("Error fetching article", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * REST API: POST /feedback/like?token=XXX
     * Stores the like in database
     */
    @PostMapping("/like")
    @ResponseBody
    public ResponseEntity<?> submitLike(@RequestParam String token) {
        try {
            logger.info("Processing like submission");

            FeedbackTokenServiceImpl.TokenPayload payload = feedbackTokenService.validateToken(token);
            feedbackTokenService.saveLike(payload.userId(), payload.articleOid());

            Map<String, Object> response = Map.of(
                    "success", true,
                    "message", "Article liked successfully!",
                    "userId", payload.userId(),
                    "articleOid", payload.articleOid()
            );

            logger.info("Like saved successfully for userId: {}, articleOid: {}",
                    payload.userId(), payload.articleOid());
            return ResponseEntity.ok(response);

        } catch (InvalidTokenException e) {
            logger.warn("Invalid token for like submission: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid token. Token may have been tampered with."));
        } catch (ExpiredTokenException e) {
            logger.warn("Expired token for like submission: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Token has expired. Please request a new newsletter."));
        } catch (Exception e) {
            logger.error("Error submitting like", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to like article: " + e.getMessage()));
        }
    }
    @GetMapping("/count")
    @ResponseBody
    public ResponseEntity<?> getLikeCount(@RequestParam Long articleOid) {
        try {
            logger.info("Fetching like count for articleOid: {}", articleOid);

            long likeCount = feedbackRepository.countByArticleOidAndLiked(articleOid, true);

            return ResponseEntity.ok(Map.of(
                    "articleOid", articleOid,
                    "likeCount", likeCount
            ));
        } catch (Exception e) {
            logger.error("Error fetching like count", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
