package com.hau.news.services;

import com.hau.news.responseBodies.EmailNewsHeadlineResponseBody;
import jakarta.mail.MessagingException;

public interface EmailNewsHeadlineService {
    void createTodayGmailHeadline (String emailAddress)throws MessagingException;
}
