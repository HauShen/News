package com.hau.news.models.exceptions;

public class TodayArticleNoFoundException extends RuntimeException{
    public TodayArticleNoFoundException(String message){
        super(message);

    }
}
