package com.hau.news.controllers;

import com.hau.news.models.NYTimesArticle;
import com.hau.news.services.NewsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/nytimesnews")
public class NyTimesNewsController {
    private final NewsService newsService;

    public NyTimesNewsController (NewsService newsService){
        this.newsService = newsService;
    }
    @GetMapping("/home")
    public List<NYTimesArticle> getNews(){
        return newsService.getTopStories();
    }
    @GetMapping("/science")
    public List<NYTimesArticle> getScienceNews(){
        return newsService.getScienceTopStories();
    }
}
