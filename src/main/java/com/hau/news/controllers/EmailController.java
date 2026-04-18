package com.hau.news.controllers;

import com.hau.news.serviceimpls.FeedbackTokenServiceImpl;
import com.hau.news.services.ArticleService;
import com.hau.news.services.EmailNewsHeadlineService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/email")
public class EmailController {

    private final EmailNewsHeadlineService emailNewsHeadlineService;
    private final FeedbackTokenServiceImpl feedbackTokenService;
    private final ArticleService articleService;
    @Autowired
    public EmailController (EmailNewsHeadlineService emailNewsHeadlineService,FeedbackTokenServiceImpl feedbackTokenService,ArticleService articleService ){
        this.emailNewsHeadlineService = emailNewsHeadlineService;
        this.feedbackTokenService = feedbackTokenService;
        this.articleService = articleService;
    }
    @PreAuthorize("hasAnyRole('NEWS_POSTER', 'ADMIN')")
    @PostMapping("/send_gmail/{emailAddress}")
    public String sendEmail(@PathVariable String emailAddress)throws MessagingException {

        emailNewsHeadlineService.createTodayGmailHeadline(emailAddress);
        return "Mail sent Successfully.";
    }
    @PreAuthorize("hasAnyRole('NEWS_POSTER', 'ADMIN')")
    @GetMapping("/send_nytimes_news/{emailAddress}")
    public String sendNYTimesNewsToEmail(@PathVariable String emailAddress)throws MessagingException{
        emailNewsHeadlineService.sendTodayNYTimesNews(emailAddress);
        return "Mail sent Successfully.";
    }
    @PreAuthorize("hasAnyRole('NEWS_POSTER', 'ADMIN')")
    @PostMapping("/send_with_feedback/{emailAddress}")
    public ResponseEntity<?> sendNewsletter(@PathVariable String emailAddress, @RequestParam String userId)throws MessagingException{
        emailNewsHeadlineService.sendEmailWithFeedback(emailAddress,userId);
        return ResponseEntity.ok("Email sent");
    }








}
