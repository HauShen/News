package com.hau.news.globalexceptionhandlers;

import com.hau.news.models.exceptions.ArticleNotFoundException;
import com.hau.news.models.exceptions.ExpiredTokenException;
import com.hau.news.models.exceptions.InvalidTokenException;
import com.hau.news.models.exceptions.TodayArticleNoFoundException;
import com.hau.news.models.exceptions.UserNotFoundException;
import jakarta.mail.MessagingException;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionsHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
    @ExceptionHandler(ArticleNotFoundException.class)
    public ResponseEntity<String> handleArticle(ArticleNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
    @ExceptionHandler(TodayArticleNoFoundException.class)
    public ResponseEntity<String> handleTodayArticle(TodayArticleNoFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<String> handleMessagingException() {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Email system error");
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<Map<String, String>> handleInvalidToken(InvalidTokenException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(ExpiredTokenException.class)
    public ResponseEntity<Map<String, String>> handleExpiredToken(ExpiredTokenException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Token has expired. Please request a new newsletter."));
    }


}
