package com.hau.news.controllers;

import com.hau.news.services.EmailNewsHeadlineService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
public class EmailController {
    @Autowired
    private EmailNewsHeadlineService emailNewsHeadlineService;
    public EmailController (EmailNewsHeadlineService emailNewsHeadlineService){
        this.emailNewsHeadlineService = emailNewsHeadlineService;
    }
    @PostMapping("/send_gmail/{emailAddress}")
    public String sendEmail(@PathVariable String emailAddress)throws MessagingException {

        emailNewsHeadlineService.createTodayGmailHeadline(emailAddress);
        return "Mail sent Successfully.";
    }

}
