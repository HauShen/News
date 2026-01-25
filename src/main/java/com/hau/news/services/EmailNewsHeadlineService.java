package com.hau.news.services;

import jakarta.mail.MessagingException;

public interface EmailNewsHeadlineService {
    void createTodayGmailHeadline (String emailAddress)throws MessagingException;
}
