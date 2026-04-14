package com.hau.news.services;

import jakarta.mail.MessagingException;

public interface EmailNewsHeadlineService {
    void createTodayGmailHeadline (String emailAddress)throws MessagingException;
    void sendTodayNYTimesNews(String emailAddress)throws MessagingException;
    //void sendEmailWithFeedback(String emailAddress,String userId)throws MessagingException;
    void sendEmailWithFeedback(String emailAddress, String userId) throws MessagingException;

}
